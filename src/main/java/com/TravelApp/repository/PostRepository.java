package com.TravelApp.repository;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import com.TravelApp.entity.Post;



public interface PostRepository extends JpaRepository<Post, Integer>{
    Post findByTitle(String title);
    List<Post> findAll(Specification<Post> specification);

}
