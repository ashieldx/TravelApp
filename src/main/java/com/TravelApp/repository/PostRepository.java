package com.TravelApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.TravelApp.entity.Post;



public interface PostRepository extends JpaRepository<Post, Integer>{
    Post findByTitle(String title);

}
