package com.TravelApp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.TravelApp.entity.User;
import com.TravelApp.repository.UserRepository;



@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileService fileService;

    public User saveUser(User user){
        return userRepository.save(user);
    }

    public List<User> getAll(){
        return userRepository.findAll();
    }

    public User editProfile(User user, User userRequest, MultipartFile file){
        user.setDOB(userRequest.getDOB());
        user.setPhone(userRequest.getPhone());
        user.setFullName(userRequest.getFullName());

        String filename = null;
        if(file != null){
            fileService.deleteFile(user.getProfileUrl());
            filename = fileService.save(file, user.getUsername());
        }

        user.setProfileUrl(filename);

        return userRepository.save(user);
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
