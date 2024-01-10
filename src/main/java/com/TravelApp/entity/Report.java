package com.TravelApp.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
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

    @ManyToOne
    private Post post;

    @Lob
    @Column(name = "message", columnDefinition = "LONGTEXT")
    private String message;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "is_processed")
    @ColumnDefault("false")
    private boolean isProcessed;
    
    
}
