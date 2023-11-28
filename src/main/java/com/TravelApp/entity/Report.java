package com.TravelApp.entity;

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
@Table(name = "mst_user_report")
public class Report {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Integer id;

    //sender id
    @Column(name = "sender_id")
    private Integer senderId;

    @Column(name = "post_id")
    private Integer postId;

    @Column(name = "message")
    private String message;

    @Column(name = "is_read")
    @ColumnDefault("false")
    private boolean isRead;
    
    
}
