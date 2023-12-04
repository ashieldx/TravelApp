package com.TravelApp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.TravelApp.dto.PostDto;
import com.TravelApp.dto.ReportDto;
import com.TravelApp.entity.Post;
import com.TravelApp.entity.Report;
import com.TravelApp.entity.User;
import com.TravelApp.service.FileService;
import com.TravelApp.service.PostService;
import com.TravelApp.service.ReportService;
import com.TravelApp.util.ErrorMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private FileService fileService;

    // Post Entity
    @PostMapping("/create")
    public Post createPost(@AuthenticationPrincipal User user, @RequestParam("files") MultipartFile[] files,
        @RequestParam("data") String postString) throws ErrorMessage, JsonMappingException, JsonProcessingException{
        //validasi file
        fileService.validateFiles(files);

        Post post = new ObjectMapper().readValue(postString, Post.class);
        return postService.savePost(user, post, files);
    }

    @PostMapping("report/{id}")
    public Report createReport(@AuthenticationPrincipal User user, @PathVariable("id") Integer id, @RequestBody ReportDto report){
        return reportService.createReport(user, id, report.getMessage());
    }

    // @PostMapping("/claim/{id}")
    // public Claim claimPost(@AuthenticationPrincipal User user, @PathVariable("id") Integer id, @RequestBody ClaimDto claim){
        
    // }

    @PutMapping("/edit/{id}")
    public Post editPost(@AuthenticationPrincipal User user, @RequestParam("files") MultipartFile[] files,
         @PathVariable("id") Integer id, @RequestParam("data") String postString) throws ErrorMessage, JsonMappingException, JsonProcessingException{
        //validasi file
        fileService.validateFiles(files);

        Post post = new ObjectMapper().readValue(postString, Post.class);
        return postService.editPost(user, id, post, files);
    }

    @DeleteMapping("/delete/{id}")
    public String deletePost(@AuthenticationPrincipal User user, @PathVariable("id") Integer id) throws ErrorMessage{
        return postService.deletePost(user, id);
    }

    @GetMapping("/get/{id}")
    public Post getPostById(@PathVariable("id") Integer id) throws ErrorMessage{
        return postService.findById(id);
    }

    @GetMapping("/getFiles/{id}/{fileNumber}")
    public ResponseEntity<Resource> getFiles(@PathVariable("id") Integer id, @PathVariable("fileNumber") Integer fileNumber){
        List<Resource> resources = postService.getFiles(id);
        if(fileNumber > resources.size()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment").body(resources.get(fileNumber-1));
    }
    
    @GetMapping("/getAll")
    public List<PostDto> getAll(){
        return postService.getAllPostsDto();
    }

}   

