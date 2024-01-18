package com.TravelApp.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.TravelApp.entity.Notification;
import com.TravelApp.entity.Post;
import com.TravelApp.entity.Review;
import com.TravelApp.entity.User;
import com.TravelApp.repository.NotificationRepository;
import com.TravelApp.repository.PostRepository;
import com.TravelApp.repository.ReviewRepository;
import com.TravelApp.repository.UserRepository;


@Service
public class NotificationService {

    public static final String USER_NOTIFICATION_LIKE = "LIKE";
    public static final String USER_NOTIFICATION_REVIEW = "REVIEW";
    public static final String USER_NOTIFICATION_LIKE_TITLE = "Someone liked your review!";
    public static final String USER_NOTIFICATION_REVIEW_TITLE = "Someone reviewed your suggestion!";
    public static final String USER_NOTIFICATION_LIKE_DESC = " liked your review";
    public static final String USER_NOTIFICATION_REVIEW_DESC = " reviewed your suggestion on ";

    public static final String USER_NOTIFICATION_ADMIN_CHAT = "Admin sent you a messsage";

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    public Page<Notification> getAllUserNotificaton(User user, Pageable pageable){
        return notificationRepository.findByUserId(user.getId(), pageable);
    }

    public List<Notification> markAllNotificationAsRead(User user){
        List<Notification> notifications = notificationRepository.findByUserId(user.getId());
        for(Notification i : notifications){
            i.setRead(true);
        }
        return notificationRepository.saveAll(notifications);
    }

    public Notification readNotification(Integer notificationId){
        Notification notification = notificationRepository.findById(notificationId).get();
        notification.setRead(true);
        return notificationRepository.save(notification);
    }

    public Notification createReviewNotification(User user, Integer postId){
        Optional<Post> post = postRepository.findById(postId);
        
        if(post.get().getUser().equals(user)){
            return null;
        }

        Notification notification = new Notification();
        notification.setCreatedDate(LocalDateTime.now());
        notification.setTitle(USER_NOTIFICATION_REVIEW_TITLE);
        notification.setCategory(USER_NOTIFICATION_REVIEW);
        notification.setMessage(user.getUsername() + USER_NOTIFICATION_REVIEW_DESC + post.get().getTitle());

        //receiver user
        notification.setUserId(post.get().getUser().getId());
        notification.setPostId(postId);

        //sender user
        notification.setSenderId(user.getId());

        return notificationRepository.save(notification);
    }

    public Notification createLikeNotification(Integer userId, Integer reviewId){
        User user  = userRepository.findFirstById(userId);
        Review review = reviewRepository.findById(reviewId).get();

        if(review.getUser().equals(user)){
            return null;
        }

        Notification notification = new Notification();
        notification.setCreatedDate(LocalDateTime.now());
        notification.setTitle(USER_NOTIFICATION_LIKE_TITLE);
        notification.setCategory(USER_NOTIFICATION_LIKE);
        notification.setMessage(user.getUsername() + USER_NOTIFICATION_LIKE_DESC);

        User reviewUser = userRepository.findFirstByUsername(review.getUser().getUsername());

        //receiver user
        notification.setUserId(reviewUser.getId());
        notification.setReviewId(reviewId);
        notification.setPostId(review.getPostId());

        //sender user
        notification.setSenderId(userId);

        return notificationRepository.save(notification);
    }

    public void deleteReviewNotification(User senderUser, Integer postId){
        Notification notification = notificationRepository.findBySenderIdAndPostId(senderUser.getId(), postId);
        notificationRepository.delete(notification);
    }

    public void deleteLikeNotification(Integer userId, Integer reviewId){
        Notification notification = notificationRepository.findBySenderIdAndReviewId(userId, reviewId);

        if(notification != null){   
            notificationRepository.delete(notification);
        }
    }

    
}
