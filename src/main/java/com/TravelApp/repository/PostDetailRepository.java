package com.TravelApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.TravelApp.entity.Post;
import com.TravelApp.entity.PostDetails;
import java.util.List;


public interface PostDetailRepository extends JpaRepository<PostDetails, Integer>{

    List<PostDetails> findByPost(Post post);
    
}
