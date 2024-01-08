package com.TravelApp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.TravelApp.entity.Notification;


public interface NotificationRepository extends JpaRepository<Notification, Integer>{
    
    List<Notification> findByUserId(Integer userId);
    Notification findBySenderIdAndReviewId(Integer senderId, Integer reviewId);
    Notification findBySenderIdAndPostId(Integer senderId, Integer postId);

    @Query(value = "SELECT COUNT(*) FROM mst_notification WHERE user_id = :userId AND is_read = false", nativeQuery = true)
    int countUnreadNotificationsForUser(Integer userId);

    @Query(value = "SELECT COUNT(*) FROM mst_notification WHERE user_id = :userId", nativeQuery = true)
    int countAllNotificationsForUser(Integer userId);
}
