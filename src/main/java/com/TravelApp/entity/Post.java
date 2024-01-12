package com.TravelApp.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "mst_post")
public class Post {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title")
    private String title;

    @Lob
    @Column(name = "description", columnDefinition = "LONGTEXT")
    private String description;

    @Column(name = "city")
    private String city;

    @Lob
    @Column(name  = "address", columnDefinition = "LONGTEXT")
    private String address;

    @Column(name = "parking")
    private String parking;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "opening_hour")
    private String openingHour;

    @Column(name = "closing_hour")
    private String closingHour;

    @Column(name = "latitude")
    @ColumnDefault("0")
    private double latitude;

    @Column(name = "longitude")
    @ColumnDefault("0")
    private double longitude;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

    @Column(name = "edit_flag")
    @ColumnDefault("false")
    private boolean editFlag;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostDetails> postDetails = new ArrayList<PostDetails>();

    @ManyToOne
    private Category category;

    @ManyToOne
    private User user;

    public boolean getEditFlag(){
        return this.editFlag;
    }
    


}
