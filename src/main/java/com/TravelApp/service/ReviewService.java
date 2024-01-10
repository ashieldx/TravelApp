package com.TravelApp.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.TravelApp.entity.Like;
import com.TravelApp.entity.Review;
import com.TravelApp.entity.ReviewDetails;
import com.TravelApp.entity.User;
import com.TravelApp.repository.LikeRepository;
import com.TravelApp.repository.ReviewDetailRepository;
import com.TravelApp.repository.ReviewRepository;
import com.TravelApp.util.ErrorMessage;

@Service
public class ReviewService {
    @Autowired
    private NotificationService  notificationService;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReviewDetailRepository reviewDetailRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    private static final String LIKE = "LIKE";
    private static final String DISLIKE = "DISLIKE";
    private static final String FILE_URL = "uploads/review-details/";
    
    public Review postReview(User user, Integer postId, Review review, MultipartFile[] files) throws Exception{

        // if(reviewRepository.findByPostIdAndUsername(postId, user.getUsername()) != null){
        //     throw new Exception("You Already Reviewed This Post");
        // }
        
        LocalDateTime currentTime = LocalDateTime.now();
        review.setCreatedDate(currentTime);
        review.setModifiedDate(currentTime);
        review.setLikes(0);
        review.setDislikes(0);
        review.setPostId(postId);
        review.setUser(user);

        List<ReviewDetails> reviewDetails = new ArrayList<>();
        Arrays.asList(files).stream().forEach(file->{
            ReviewDetails reviewDetail = new ReviewDetails();
            reviewDetail.setOriginalFileName(file.getOriginalFilename());
            reviewDetail.setFileType(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1));
            reviewDetail.setFileName(review.getPostId()+ "_R_" + review.getUser().getUsername() + "_" + reviewDetails.size() + "." + reviewDetail.getFileType());
            reviewDetail.setReview(review);
            reviewDetail.setCreatedDate(currentTime);
            reviewDetail.setUrl(FILE_URL);

            fileService.save(file, reviewDetail);
            reviewDetails.add(reviewDetail);
        });

        review.setReviewDetails(reviewDetails);
        notificationService.createReviewNotification(user, postId);
        return reviewRepository.save(review);
    }
    
    public Review editReview(User user, Integer reviewId, Review newReview, MultipartFile[] files){
        Review review = reviewRepository.findById(reviewId).get();
        LocalDateTime currTime = LocalDateTime.now();

        if(!review.getUser().equals(user)){
            //cannot edit review
            return review;
        }

        review.setEditFlag(true);
        review.setDescription(newReview.getDescription());
        review.setRating(newReview.getRating());

        //remove old files
        List<ReviewDetails> oldReviewDetails = reviewDetailRepository.findByReview(review);
        for(ReviewDetails i : oldReviewDetails){
            fileService.deleteFile(FILE_URL+i.getFileName());
        }
        reviewDetailRepository.deleteAll(oldReviewDetails);
        
        List<ReviewDetails> newReviewDetails = new ArrayList<>();
        Arrays.asList(files).stream().forEach(file->{
            ReviewDetails reviewDetail = new ReviewDetails();
            reviewDetail.setOriginalFileName(file.getOriginalFilename());
            reviewDetail.setFileType(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1));
            reviewDetail.setFileName(review.getPostId()+ "_R_" + review.getUser().getUsername() + "_" + newReviewDetails.size() + "." + reviewDetail.getFileType());
            reviewDetail.setReview(review);
            reviewDetail.setCreatedDate(currTime);
            reviewDetail.setUrl(FILE_URL);

            fileService.save(file, reviewDetail);
            newReviewDetails.add(reviewDetail);
        });

        review.setReviewDetails(newReviewDetails);
        return reviewRepository.save(review);
    }

    public Review likeReview(Integer userId, Integer reviewId){
        Like like = likeRepository.findByUserIdAndReviewId(userId, reviewId);
        Review review = reviewRepository.findById(reviewId).get();
        LocalDateTime currTime = LocalDateTime.now();

        if(like == null){
            Like newLike = new Like();
            newLike.setReviewId(reviewId);
            newLike.setUserId(userId);
            newLike.setAction(LIKE);
            newLike.setCreatedDate(currTime);
            likeRepository.save(newLike);
            notificationService.createLikeNotification(userId, reviewId);
        }
        else if(like.getAction().equals(DISLIKE)){
            like.setAction(LIKE);
            like.setCreatedDate(currTime);
            likeRepository.save(like);
            notificationService.createLikeNotification(userId, reviewId);
        }
        else{
            likeRepository.delete(like);
            notificationService.deleteLikeNotification(userId, reviewId);
        }


        Integer likeCount = likeRepository.getLikeOrDislikeCount(reviewId, LIKE);
        Integer dislikeCount = likeRepository.getLikeOrDislikeCount(reviewId, DISLIKE);
        review.setLikes(likeCount);
        review.setDislikes(dislikeCount);
        review.setModifiedDate(LocalDateTime.now());
        return reviewRepository.save(review);
    }

    public Review dislikeReview(Integer userId, Integer reviewId){
        Like like = likeRepository.findByUserIdAndReviewId(userId, reviewId);
        Review review  = reviewRepository.findById(reviewId).get();
        LocalDateTime currTime = LocalDateTime.now();

        if(like == null){
            Like newLike = new Like();
            newLike.setReviewId(reviewId);
            newLike.setUserId(userId);
            newLike.setAction(DISLIKE);
            newLike.setCreatedDate(currTime);
            likeRepository.save(newLike);
        }
        else if(like.getAction().equals(LIKE)){
            like.setAction(DISLIKE);
            like.setCreatedDate(currTime);
            likeRepository.save(like);
        }
        else{
            likeRepository.delete(like);
        }

        notificationService.deleteLikeNotification(userId, reviewId);
        Integer likeCount = likeRepository.getLikeOrDislikeCount(reviewId, LIKE);
        Integer dislikeCount = likeRepository.getLikeOrDislikeCount(reviewId, DISLIKE);
        review.setLikes(likeCount); 
        review.setDislikes(dislikeCount);
        review.setModifiedDate(LocalDateTime.now());
        return reviewRepository.save(review);
    }

    public Page<Review> getPostReviews(int id, Pageable pageable){
        return reviewRepository.findByPostId(id, pageable);
    }

    public List<User> getUsersLikedReview(int id){
        List<Like> likeList = likeRepository.findByReviewId(id);

        List<Integer> userIdList = new ArrayList<Integer>();
        for(Like i : likeList){
            userIdList.add(i.getUserId());
        }
        
        return userService.findUsers(userIdList);
    }

    public String deleteReview(User user, Integer reviewId) throws ErrorMessage{
        Review review = reviewRepository.findById(reviewId).get();
    
        if(review.getUser() == null || review.getUser().equals(user)){
            List<ReviewDetails> reviewDetails = reviewDetailRepository.findByReview(review);
            for(ReviewDetails i : reviewDetails){
                fileService.deleteFile(FILE_URL+i.getFileName());
            }
            reviewDetailRepository.deleteAll(reviewDetails);
            reviewRepository.delete(review);
            return "Delete Successful";
        }
        else{
            throw new ErrorMessage("Review Belongs To Another User");
        }
    }

    

}
