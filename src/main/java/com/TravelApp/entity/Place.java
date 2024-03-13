package com.TravelApp.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;

import io.micrometer.common.lang.Nullable;
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
public class Place {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title")
    private String title;

    @Nullable
    @Column(name = "alias")
    private String alias;

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

    @Lob
    @Column(name = "opening_hour", columnDefinition = "LONGTEXT")
    private String openingHour;

    @Lob
    @Column(name = "closing_hour", columnDefinition = "LONGTEXT")
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

    @Column(name = "claim_flag")
    @ColumnDefault("false")
    private boolean verified;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PlaceDetails> postDetails = new ArrayList<PlaceDetails>();

    @ManyToOne
    private Category category;

    @ManyToOne
    private User user;

    @ManyToOne
    private User creator;

    public boolean getEditFlag(){
        return this.editFlag;
    }

    


}
