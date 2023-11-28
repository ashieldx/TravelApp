package com.TravelApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.TravelApp.entity.Review;
import java.util.List;


public interface ReviewRepository extends JpaRepository<Review, Integer>{

    @Query(value = "SELECT AVG(rating) FROM mst_review WHERE post_id = :postId", nativeQuery = true)
    public float getPostAverageRating(Integer postId);

    @Query(value = "SELECT COUNT(*) FROM mst_review WHERE post_id = :postId", nativeQuery = true)
    public int getPostTotalRating(Integer postId);

    List<Review> findByPostId(Integer postId);

    Review findByPostIdAndUsername(Integer postId, String username);
}
