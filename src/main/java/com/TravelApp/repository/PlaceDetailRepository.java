package com.TravelApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.TravelApp.entity.Place;
import com.TravelApp.entity.PlaceDetails;
import java.util.List;


public interface PlaceDetailRepository extends JpaRepository<PlaceDetails, Integer>{

    List<PlaceDetails> findByPost(Place post);
    
}
