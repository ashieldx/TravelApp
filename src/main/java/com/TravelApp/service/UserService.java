package com.TravelApp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.TravelApp.entity.User;
import com.TravelApp.repository.UserRepository;



@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User saveUser(User user){
        return userRepository.save(user);
    }

    public List<User> getAll(){
        return userRepository.findAll();
    }

    public void inactivateUser(User user){
        user.setActive(false);
        userRepository.save(user);
    }

    public void inactivateUser(String username){
        User user = userRepository.findFirstByUsername(username);
        if(user != null){
            user.setActive(false);
            userRepository.save(user);
        }
    }
}
