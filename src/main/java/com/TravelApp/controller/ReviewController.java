package com.TravelApp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.TravelApp.entity.Review;
import com.TravelApp.entity.User;
import com.TravelApp.service.FileService;
import com.TravelApp.service.ReviewService;
import com.TravelApp.util.ErrorMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/review")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @Autowired
    private FileService fileService;

    private static final long MAX_FILE_SIZE = 10485760;

    @PostMapping("/post/{id}")
    public Review postReview(@AuthenticationPrincipal User user, @PathVariable("id") Integer postId, @RequestParam("files") MultipartFile[] files, 
        @RequestParam("data") String reviewString) throws Exception{
        for(MultipartFile i : files){
            if(!fileService.validateFileType(i)){
                throw new ErrorMessage("Invalid file Type!");
            }
            if(i.getSize() > MAX_FILE_SIZE){
                throw new ErrorMessage("Files(s) too large!");
            }
        }
        
        Review review = new ObjectMapper().readValue(reviewString, Review.class);
        return reviewService.postReview(user, postId, review, files);
    }

    @PutMapping("/edit/{id}")
    public Review editPost(@AuthenticationPrincipal User user, @RequestParam("files") MultipartFile[] files,
         @PathVariable("id") Integer id, @RequestParam("data") String reviewString) throws ErrorMessage, JsonMappingException, JsonProcessingException{
        //validasi file
        fileService.validateFiles(files);

        Review review = new ObjectMapper().readValue(reviewString, Review.class);
        return reviewService.editReview(user, id, review, files);
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
