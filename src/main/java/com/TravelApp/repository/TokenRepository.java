package com.TravelApp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.TravelApp.entity.Token;


public interface TokenRepository extends JpaRepository<Token, Integer> {

    Token findFirstByUserId(Integer userId);
    Optional<Token> findByToken(String token);
}
