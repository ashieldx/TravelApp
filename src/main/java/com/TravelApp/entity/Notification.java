package com.TravelApp.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "mst_notification")
public class Notification {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Integer id; 

    @Column(name = "post_id")
    private Integer postId;

    @Column(name = "review_id")
    private Integer reviewId;

    //tambahin sender it (buat cegah duplicate)
    @Column(name = "sender_id")
    private Integer senderId;

    //user id tujuan
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "title")
    public String title;

    @Column(name = "category")
    private String category;

    @Column(name = "message")
    private String message;

    @Column(name = "is_read")
    @ColumnDefault("false")
    private boolean isRead;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    public boolean getRead(){
        return this.isRead;
    }



}
