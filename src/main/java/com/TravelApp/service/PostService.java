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
import com.TravelApp.repository.PostDetailRepository;
import com.TravelApp.repository.PostRepository;
import com.TravelApp.repository.ReviewRepository;
import com.TravelApp.util.ErrorMessage;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostDetailRepository postDetailRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private ReviewRepository reviewRepository;

    private static final String FILE_URL = "uploads/post-details/";

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
            postDetail.setOriginalFileName(file.getOriginalFilename());
            postDetail.setFileType(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1));
            postDetail.setFileName(post.getTitle() + "_" + postDetails.size() + "." + postDetail.getFileType());
            postDetail.setPost(post);
            postDetail.setCreatedDate(currentTime);
            postDetail.setUrl(FILE_URL);
            fileService.save(file, postDetail);
            postDetails.add(postDetail);
        });

        post.setPostDetails(postDetails);
        return postRepository.save(post);
    }

    public Post editPost(User user, Integer postId, Post newPost, MultipartFile[] files) throws ErrorMessage{
        Post post = this.findById(postId);
        LocalDateTime currentTime = LocalDateTime.now();

        if(!post.getUser().equals(user)){
            //cannot edit post
            return post;
        }

        post.setEditFlag(true);
        post.setDescription(newPost.getDescription());
        post.setPostDetails(newPost.getPostDetails());
        post.setAddress(newPost.getAddress());
        post.setOpeningHour(newPost.getOpeningHour());
        post.setClosingHour(newPost.getClosingHour());
        post.setPhoneNumber(newPost.getPhoneNumber());
        post.setModifiedDate(currentTime);

        //remove old Files
        List<PostDetails> oldDetails = postDetailRepository.findByPost(post);
        for(PostDetails i : oldDetails){ 
            fileService.deleteFile(FILE_URL+i.getFileName());
        }
        postDetailRepository.deleteAll(oldDetails);

        //add new Files
        List<PostDetails> newPostDetails = new ArrayList<>();
        Arrays.asList(files).stream().forEach(file-> {
            PostDetails postDetail = new PostDetails();
            postDetail.setOriginalFileName(file.getOriginalFilename());
            postDetail.setFileType(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1));
            postDetail.setFileName(post.getTitle() + "_" + newPostDetails.size() + "." + postDetail.getFileType());
            postDetail.setPost(post);
            postDetail.setCreatedDate(currentTime);
            postDetail.setUrl(FILE_URL);
            fileService.save(file, postDetail);
            newPostDetails.add(postDetail);
        });

        post.setPostDetails(newPostDetails);
        return postRepository.save(post);
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
                (i.getPostDetails().size() == 0 ? 
                    "No Image" : 
                    i.getPostDetails().get(0).getUrl()+
                    i.getPostDetails().get(0).getFileName()),
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
                resources.add(fileService.getFileByName(postDetails.getFileName(), postDetails.getUrl()));
            }
        }
        return resources;
    }

    public String deletePost(User user, Integer postId) throws ErrorMessage{
        Post post = this.findById(postId);

        if(post.getUser().equals(user)){
            List<PostDetails> postDetails = postDetailRepository.findByPost(post);
            for(PostDetails i : postDetails){
                fileService.deleteFile(FILE_URL+i.getFileName());
            }
            postDetailRepository.deleteAll(postDetails);
            postRepository.delete(post);
            return "Delete Successful";
        }
        else{
            return "Post Belongs To Another User";
        }
    }

    /* ADMIN SIDE */

    public String deletePostByAdmin(Integer id){

        //send Notification to user
        Post post = postRepository.findById(id).get();
        
        if(post != null){
            try{
                deletePost(post.getUser(), id);
            }
            catch(Exception e){
                return "FAILED TO DELETE POST";
            }
            return "SUCCESS DELETE";
        }
        return "FAILED TO DELETE POST";
    }

}
