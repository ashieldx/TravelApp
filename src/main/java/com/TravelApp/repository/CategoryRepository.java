package com.TravelApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.TravelApp.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer>{
    
}
