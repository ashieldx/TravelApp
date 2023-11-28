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

    @Column(name = "description")
    private String description;

    @Column(name  = "address")
    private String address;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "opening_hour")
    private String openingHour;

    @Column(name = "closing_hour")
    private String closingHour;

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
