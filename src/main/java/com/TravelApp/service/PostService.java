package com.TravelApp.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.TravelApp.dto.PostDto;
import com.TravelApp.entity.Post;
import com.TravelApp.entity.PostDetails;
import com.TravelApp.entity.User;
import com.TravelApp.repository.PostRepository;
import com.TravelApp.repository.ReviewRepository;
import com.TravelApp.util.ErrorMessage;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private ReviewRepository reviewRepository;

    public Post savePost(User user, Post post, MultipartFile[] files) throws ErrorMessage{
        if(postRepository.findByTitle(post.getTitle()) != null){
            throw new ErrorMessage("Duplicate Title");
        }
        
        LocalDateTime currentTime = LocalDateTime.now();
        post.setUser(user);
        post.setCreatedDate(currentTime);
        post.setModifiedDate(currentTime);

        List<PostDetails> postDetails = new ArrayList<>();
        Arrays.asList(files).stream().forEach(file-> {
            PostDetails postDetail = new PostDetails();
            postDetail.setFileName(postDetails.size()+1 + "_" + post.getTitle() + "_" + file.getOriginalFilename());
            postDetail.setOriginalFileName(file.getOriginalFilename());
            postDetail.setPost(post);
            postDetail.setCreatedDate(currentTime);
            postDetail.setUrl("/uploads/post-details/"+postDetail.getFileName());

            fileService.save(file, postDetail);
            postDetails.add(postDetail);
        });

        post.setPostDetails(postDetails);
        return postRepository.save(post);
    }

    public Post editPost(Integer postId, Post newPost) throws ErrorMessage{
        Post post = this.findById(postId);

        post.setEditFlag(true);
        post.setDescription(newPost.getDescription());
        post.setPostDetails(newPost.getPostDetails());
        post.setAddress(newPost.getAddress());
        post.setOpeningHour(newPost.getOpeningHour());
        post.setClosingHour(newPost.getClosingHour());
        post.setPhoneNumber(newPost.getPhoneNumber());
        post.setModifiedDate(LocalDateTime.now());

        return postRepository.save(post);
    }

    public String deletePost(User user, Integer postId) throws ErrorMessage{
        Post post = this.findById(postId);

        if(post.getUser().equals(user)){
            postRepository.delete(post);

            return "Delete Successful";
        }
        else{
            throw new ErrorMessage("Post Belongs To Another User");
        }
    }

    public List<PostDto> getAllPostsDto(){
        List<Post> posts = postRepository.findAll();
        List<PostDto> postDtoList = new ArrayList<PostDto>();
        
        //map Post to PostDto for preview
        for(Post i : posts){
            float rating = 0, rounded = 0;
            Integer totalRating = reviewRepository.getPostTotalRating(i.getId());
            if(totalRating > 0){
                rating = reviewRepository.getPostAverageRating(i.getId());
            }
            rounded = Math.round(rating*100)/100.f;
            postDtoList.add(new PostDto(i.getId(), 
                i.getTitle(), 
                i.getDescription(), 
                i.getPostDetails().get(0).getFileName(),
                i.getCreatedDate(), 
                i.getEditFlag(), 
                rounded, 
                totalRating
                )
            );
        }
        return postDtoList;
    }

    public Post findById(Integer id) throws ErrorMessage{
        Optional<Post> post = postRepository.findById(id);
        if(post.isPresent()){
            return post.get();
        }
        throw new ErrorMessage("Post Not Found");
    }

    public List<Post> getAll(){
        return postRepository.findAll();
    }

    public List<Resource> getFiles(Integer postId){
        List<Resource> resources = new ArrayList<>();
        Optional<Post> post = postRepository.findById(postId);
        if(post.isPresent()){
            for(PostDetails postDetails : post.get().getPostDetails()){
                resources.add(fileService.getFileByName(postDetails.getFileName()));
            }
        }
        return resources;
    }

    /* ADMIN SIDE */

    public String deletePostByAdmin(Integer id){

        //send Notification to user
        Post post = postRepository.findById(id).get();
        
        if(post != null){
            postRepository.delete(post);
            return "SUCCESS";
        }
        return "FAILED TO DELETE POST";
    }
}
