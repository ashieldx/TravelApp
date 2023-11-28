package com.TravelApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.TravelApp.entity.User;

public interface UserRepository extends JpaRepository<User, Integer>{
   User findFirstByUsername(String username);
   User findFirstById(Integer id);
}

