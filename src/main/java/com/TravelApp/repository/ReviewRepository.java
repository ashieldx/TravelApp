package com.TravelApp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.TravelApp.entity.Review;
import com.TravelApp.entity.User;


public interface ReviewRepository extends JpaRepository<Review, Integer>{

    @Query(value = "SELECT AVG(rating) FROM mst_review WHERE post_id = :postId", nativeQuery = true)
    public float getPostAverageRating(Integer postId);

    @Query(value = "SELECT COUNT(*) FROM mst_review WHERE post_id = :postId", nativeQuery = true)
    public int getPostTotalRating(Integer postId);

    @Query(value = "SELECT COUNT(*) FROM mst_review WHERE post_id = :postId AND MONTH(created_date) = MONTH(CURRENT_DATE) AND YEAR(created_date) = YEAR(CURRENT_DATE)", nativeQuery = true)
    int getPostTotalRatingThisMonth(@Param("postId") Integer postId);

    Page<Review> findByPostId(Integer postId, Pageable pageable);

    Review findByPostIdAndUser(Integer postId, User user);
}
