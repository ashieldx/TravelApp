package com.TravelApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.TravelApp.entity.Notification;
import java.util.List;


public interface NotificationRepository extends JpaRepository<Notification, Integer>{
    
    List<Notification> findByUserId(Integer userId);
    Notification findBySenderIdAndReviewId(Integer senderId, Integer reviewId);
    Notification findBySenderIdAndPostId(Integer senderId, Integer postId);

}
