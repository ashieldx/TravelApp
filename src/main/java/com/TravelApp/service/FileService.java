package com.TravelApp.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileSystems;
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
import com.TravelApp.entity.ReviewDetails;
import com.TravelApp.util.ErrorMessage;

@Service
public class FileService{

    private final Path postRoot = Paths.get("uploads/post-details");
    private final Path reviewRoot = Paths.get("uploads/review-details");
    private final Path claimRoot = Paths.get("uploads/claim-details");

    private static final long MAX_FILE_SIZE = 10485760;

    public Resource getFileByName(String filename, String url) {
        try{
            Path rootPath = Paths.get(url);
            Path filePath = rootPath.resolve(filename);
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists() || resource.isReadable()){
                return resource;
                
            }
            else{
                throw new RuntimeException("Cannot read File(s) ");
            }
        }catch(MalformedURLException e){
            throw new RuntimeException("Error retrieving File: " + e.getMessage());
        }
    }

    public void save(MultipartFile file, PostDetails postDetails) {
        try{
            Files.copy(file.getInputStream(), 
                this.postRoot.resolve(Objects.requireNonNull(postDetails.getFileName())));

        }catch(Exception e){
            throw new RuntimeException("Error creating File: " + e.getMessage());
        }
    }

    public void save(MultipartFile file, ReviewDetails reviewDetails) {
        try{
            Files.copy(file.getInputStream(), 
                this.reviewRoot.resolve(Objects.requireNonNull(reviewDetails.getFileName())));

        }catch(Exception e){
            throw new RuntimeException("Error creating File: " + e.getMessage());
        }
    }

    public void save(MultipartFile file, ClaimDetails claimDetails) {
        try{
            Files.copy(file.getInputStream(), 
                this.claimRoot.resolve(Objects.requireNonNull(claimDetails.getFileName())));

        }catch(Exception e){
            throw new RuntimeException("Error creating File: " + e.getMessage());
        }
    }

    public void validateFiles(MultipartFile[] files) throws ErrorMessage{
        for(MultipartFile i : files){
            if(!this.validateFileType(i)){
                throw new ErrorMessage("Invalid file Type!");
            }
            if(i.getSize() > MAX_FILE_SIZE){
                throw new ErrorMessage("Files(s) too large!");
            }
        }
    }

    public boolean validateFileType(MultipartFile file){
        String type = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
        if(type.equalsIgnoreCase("jpg")
            || type.equalsIgnoreCase("png") 
            || type.equalsIgnoreCase("jpeg")){
                return true;
            }
        return false;
    }

    public void deleteFile(String url){
        Path path = FileSystems.getDefault().getPath(url);
        try{
            Files.deleteIfExists(path);
        }catch(IOException e){
            throw new RuntimeException("Error deleting File: " + e.getMessage());
        }
    }




    
}
