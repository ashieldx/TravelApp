package com.TravelApp.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import com.TravelApp.dto.SortDto;
import com.TravelApp.entity.Place;
import com.TravelApp.entity.User;



public interface PlaceRepository extends JpaRepository<Place, Integer>{
    Place findByTitle(String title);

    List<Place> findAll(Specification<SortDto> specification);

    Page<Place> findByUser(User user, Pageable pageable);
}
