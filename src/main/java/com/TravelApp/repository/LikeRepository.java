package com.TravelApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.TravelApp.entity.Like;
import java.util.List;


public interface LikeRepository extends JpaRepository<Like, Integer>{
    Like findByUserIdAndReviewId(Integer userId, Integer reviewId);
    
    List<Like> findByReviewId(Integer reviewId);

    @Query(value = "SELECT COUNT(*) FROM mst_review_like WHERE review_id = :reviewId AND action = :action", nativeQuery = true)
    public Integer getLikeOrDislikeCount(Integer reviewId, String action);
    
}
