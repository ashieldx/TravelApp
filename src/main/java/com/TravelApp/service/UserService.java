package com.TravelApp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.TravelApp.dto.PostDto;
import com.TravelApp.entity.User;
import com.TravelApp.repository.UserRepository;



@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private PlaceService postService;
    

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
        user.setEmail(userRequest.getEmail());

        String filename = null;
        if(file != null){
            if(!user.getProfileUrl().equals("default.jpg")){
                fileService.deleteFile(user.getProfileUrl());
            }
            filename = fileService.save(file, user.getUsername());
        }

        user.setProfileUrl(filename);

        return userRepository.save(user);
    }

    public Page<PostDto> getPostsByUser(String username, Pageable pageable){
        User user = this.findByUsername(username);
        return postService.getPostByUser(user, pageable);
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

    public User findByUsername(String username){
        return userRepository.findFirstByUsername(username);
    }

    public List<User> findUsers(List<Integer> userIdList){
        return userRepository.findAllById(userIdList);
    }

    public boolean validateEmailEdit(User userEdit, String email){
        User user  = userRepository.findFirstByEmail(email);
        if(user != null){
            if(userEdit.equals(user)){
                return true;
            }
            return false;
        }
        return true;
    }


}
