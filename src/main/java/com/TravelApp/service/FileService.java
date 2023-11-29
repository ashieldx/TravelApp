package com.TravelApp.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.TravelApp.entity.ClaimDetails;
import com.TravelApp.entity.PostDetails;

@Service
public class FileService{

    private final Path postDetailRoot = Paths.get("uploads/post-details");
    private final Path claimDetailRoot = Paths.get("uploads/claim-details");
    private final Path reviewDetailRoot = Paths.get("uploads/review-details");

    public void initialize(Path root){
        try{
            File directory = new File(root.toUri());
            boolean ifExist = directory.exists();
            if(!ifExist){
                Files.createDirectory(root);
            }      
        }catch (IOException e){
            throw new RuntimeException("Trouble Creating Directory!");
        }
    }

    /*
    POST DETAILS 
    */
    public void save(MultipartFile file, PostDetails postDetails) {
        initialize(postDetailRoot);
        try{
            Files.copy(file.getInputStream(), 
                this.postDetailRoot.resolve(Objects.requireNonNull(postDetails.getFileName())));

        }catch(Exception e){
            throw new RuntimeException("Error message : " + e.getMessage());
        }
    }

    public Resource getFileByName(String filename) {
        try{
            Path filePath = postDetailRoot.resolve(filename);
            Resource resource = new UrlResource(filePath.toUri());

            if(resource.exists() || resource.isReadable()){
                return resource;
            }
            else{
                throw new RuntimeException("Cannot read File(s)");
            }
        }catch(MalformedURLException e){
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    /* 
    REVIEW DETAILS   
    */
    

    /*
    CLAIM DETAILS
    */
     public void save(MultipartFile file, ClaimDetails claimDetails) {
        initialize(claimDetailRoot);
        try{
            Files.copy(file.getInputStream(), 
                this.claimDetailRoot.resolve(Objects.requireNonNull(claimDetails.getFileName())));

        }catch(Exception e){
            throw new RuntimeException("Error message : " + e.getMessage());
        }
    }



    
}
