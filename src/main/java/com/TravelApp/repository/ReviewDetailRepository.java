package com.TravelApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.TravelApp.entity.Review;
import com.TravelApp.entity.ReviewDetails;
import java.util.List;


public interface ReviewDetailRepository extends JpaRepository<ReviewDetails, Integer>{
    
    List<ReviewDetails> findByReview(Review review);
}
