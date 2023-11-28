package com.TravelApp.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.TravelApp.config.JwtService;
import com.TravelApp.dto.AuthResponse;
import com.TravelApp.dto.UserLoginDto;
import com.TravelApp.entity.Token;
import com.TravelApp.entity.User;
import com.TravelApp.repository.TokenRepository;
import com.TravelApp.repository.UserRepository;
import com.TravelApp.util.ErrorMessage;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private static final String BEARER_TOKEN_CONSTANT = "BEARER";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;


    @PostMapping("/register")
    public AuthResponse register(@RequestBody User userRequest) throws ErrorMessage{
        //Validation for duplicate username
        if(userRepository.findFirstByUsername(userRequest.getUsername()) != null){
            throw new ErrorMessage("Username Already Exist");
        }

        String encrypted = passwordEncoder.encode(userRequest.getPassword());
        userRequest.setPassword(encrypted);
        userRequest.setRole("USER");
        userRequest.setCreatedDate(LocalDateTime.now());
        User savedUser = userRepository.save(userRequest);   

        String jwtToken = jwtService.generateToken(userRequest);
        saveToken(savedUser, jwtToken);

        AuthResponse authResponse = new AuthResponse(userRequest, jwtToken);
        return authResponse;

    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody UserLoginDto user) throws ErrorMessage{
        if(userRepository.findFirstByUsername(user.getUsername()) == null){
            throw new ErrorMessage("Username Not Found");
        }
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        User authenticatedUser = userRepository.findFirstByUsername(user.getUsername());
        String jwtToken = jwtService.generateToken(authenticatedUser);
        saveToken(authenticatedUser, jwtToken);

        AuthResponse authResponse = new AuthResponse(authenticatedUser, jwtToken);
        return authResponse;

    }

    private void saveToken(User user, String jwtToken){
        Token existingToken = tokenRepository.findFirstByUserId(user.getId());
        
        if(existingToken == null){
            Token token = new Token();
            token.setUserId(user.getId());
            token.setTokenType(BEARER_TOKEN_CONSTANT);
            token.setToken(jwtToken);
            token.setCreatedDate(LocalDateTime.now());
            tokenRepository.save(token);
        }
        else{
            existingToken.setCreatedDate(LocalDateTime.now());
            existingToken.setToken(jwtToken);
            existingToken.setExpired(false);
            tokenRepository.save(existingToken);
        }
        return;
    }

}
