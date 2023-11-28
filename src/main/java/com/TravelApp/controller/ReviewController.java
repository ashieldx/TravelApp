package com.TravelApp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.TravelApp.entity.Review;
import com.TravelApp.entity.User;
import com.TravelApp.service.ReviewService;
import com.TravelApp.util.ErrorMessage;

@RestController
@RequestMapping("/review")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @PostMapping("/post/{id}")
    public Review postReview(@AuthenticationPrincipal User user, @PathVariable("id") Integer postId, @RequestBody Review review) throws Exception{
        return reviewService.postReview(user, postId, review);
    }

    @GetMapping("/get/{id}")
    public List<Review> getPostReviews(@PathVariable("id") Integer id){
        return reviewService.getPostReviews(id);
    }

    @PostMapping("like/{id}")
    public Review likeReview(@AuthenticationPrincipal User user, @PathVariable("id") Integer reviewId){
        return reviewService.likeReview(user.getId(), reviewId);
    }

    @PostMapping("dislike/{id}")
    public Review dislikeReview(@AuthenticationPrincipal User user, @PathVariable("id") Integer reviewId){
        return reviewService.dislikeReview(user.getId(), reviewId);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteReview(@AuthenticationPrincipal User user, @PathVariable("id") Integer id) throws ErrorMessage{
        return reviewService.deleteReview(user, id);
    }

    

}
