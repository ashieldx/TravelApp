package com.TravelApp.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import com.TravelApp.dto.SortDto;
import com.TravelApp.entity.Post;
import com.TravelApp.entity.User;



public interface PostRepository extends JpaRepository<Post, Integer>{
    Post findByTitle(String title);

    List<Post> findAll(Specification<SortDto> specification);

    Page<Post> findByUser(User user, Pageable pageable);
}
