package com.TravelApp.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.TravelApp.entity.Like;
import com.TravelApp.entity.Review;
import com.TravelApp.entity.ReviewDetails;
import com.TravelApp.entity.User;
import com.TravelApp.repository.LikeRepository;
import com.TravelApp.repository.ReviewRepository;
import com.TravelApp.util.ErrorMessage;

@Service
public class ReviewService {
    @Autowired
    private NoticationService  noticationService;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private FileService fileService;

    private static final String LIKE = "LIKE";
    private static final String DISLIKE = "DISLIKE";
    
    public Review postReview(User user, Integer postId, Review review, MultipartFile[] files) throws Exception{

        if(reviewRepository.findByPostIdAndUsername(postId, user.getUsername()) != null){
            throw new Exception("You Already Reviewed This Post");
        }
        
        LocalDateTime currentTime = LocalDateTime.now();
        review.setCreatedDate(currentTime);
        review.setModifiedDate(currentTime);
        review.setLikes(0);
        review.setDislikes(0);
        review.setPostId(postId);
        review.setUsername(user.getUsername());

        List<ReviewDetails> reviewDetails = new ArrayList<>();
        Arrays.asList(files).stream().forEach(file->{
            ReviewDetails reviewDetail = new ReviewDetails();
            reviewDetail.setOriginalFileName(file.getOriginalFilename());
            reviewDetail.setFileType(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1));
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm");
            reviewDetail.setFileName(currentTime.format(dateTimeFormatter) + 
                                "_" + (reviewDetails.size()+1) + 
                                "." + reviewDetail.getFileType());
            reviewDetail.setReview(review);
            reviewDetail.setCreatedDate(currentTime);
            reviewDetail.setUrl("/uploads/review-details/");

            fileService.save(file, reviewDetail);
            reviewDetails.add(reviewDetail);
        });

        review.setReviewDetails(reviewDetails);
        noticationService.createReviewNotification(user, postId);
        return reviewRepository.save(review);
    }



    public Review likeReview(Integer userId, Integer reviewId){
        Like like = likeRepository.findByUserIdAndReviewId(userId, reviewId);
        Review review = reviewRepository.findById(reviewId).get();

        if(like == null){
            Like newLike = new Like();
            newLike.setReviewId(reviewId);
            newLike.setUserId(userId);
            newLike.setAction(LIKE);
            likeRepository.save(newLike);
            noticationService.createLikeNotification(userId, reviewId);
        }
        else if(like.getAction().equals(DISLIKE)){
            like.setAction(LIKE);
            likeRepository.save(like);
            noticationService.createLikeNotification(userId, reviewId);
        }
        else{
            likeRepository.delete(like);
            noticationService.deleteLikeNotification(userId, reviewId);
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

        if(like == null){
            Like newLike = new Like();
            newLike.setReviewId(reviewId);
            newLike.setUserId(userId);
            newLike.setAction(DISLIKE);
            likeRepository.save(newLike);
        }
        else if(like.getAction().equals(LIKE)){
            like.setAction(DISLIKE);
            likeRepository.save(like);
            likeRepository.save(like);
        }
        else{
            likeRepository.delete(like);
        }

        noticationService.deleteLikeNotification(userId, reviewId);
        Integer likeCount = likeRepository.getLikeOrDislikeCount(reviewId, LIKE);
        Integer dislikeCount = likeRepository.getLikeOrDislikeCount(reviewId, DISLIKE);
        review.setLikes(likeCount);
        review.setDislikes(dislikeCount);
        review.setModifiedDate(LocalDateTime.now());
        return reviewRepository.save(review);
    }

    public List<Review> getPostReviews(int id){
        return reviewRepository.findByPostId(id);
    }

    public String deleteReview(User user, Integer reviewId) throws ErrorMessage{
        Review review = reviewRepository.findById(reviewId).get();
    
        if(review.getUsername() == null || review.getUsername().equals(user.getUsername())){
            reviewRepository.delete(review);
            return "Delete Successful";
        }
        else{
            throw new ErrorMessage("Review Belongs To Another User");
        }
    }

}
