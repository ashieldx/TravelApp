package com.TravelApp.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.TravelApp.dto.GeolocationRequest;
import com.TravelApp.dto.PostDto;
import com.TravelApp.dto.SortDto;
import com.TravelApp.entity.Claim;
import com.TravelApp.entity.Place;
import com.TravelApp.entity.PlaceDetails;
import com.TravelApp.entity.Report;
import com.TravelApp.entity.User;
import com.TravelApp.repository.ClaimRepository;
import com.TravelApp.repository.PlaceDetailRepository;
import com.TravelApp.repository.PlaceRepository;
import com.TravelApp.repository.ReportRepository;
import com.TravelApp.repository.ReviewRepository;
import com.TravelApp.specification.PlaceSpecification;
import com.TravelApp.util.ErrorMessage;

@Service
public class PlaceService {
    @Autowired
    private PlaceRepository postRepository;

    @Autowired
    private PlaceDetailRepository postDetailRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private ClaimRepository claimRepository;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private GeolocationService geolocationService;

    private static final String FILE_URL = "uploads/post-details/";

    public Place savePost(User user, Place post, MultipartFile[] files) throws ErrorMessage{
        if(postRepository.findByTitle(post.getTitle()) != null){
            throw new ErrorMessage("Duplicate Title");
        }
        
        LocalDateTime currentTime = LocalDateTime.now();
        post.setCreator(user);
        post.setUser(user);
        post.setCreatedDate(currentTime);
        post.setModifiedDate(currentTime);


        List<PlaceDetails> postDetails = new ArrayList<>();
        Arrays.asList(files).stream().forEach(file-> {
            PlaceDetails postDetail = new PlaceDetails();
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

    public Place editPost(User user, Integer postId, Place newPost, MultipartFile[] files) throws ErrorMessage{
        Place post = this.findById(postId);
        LocalDateTime currentTime = LocalDateTime.now();

        if(!post.getUser().equals(user)){
            //cannot edit post
            return post;
        }

        post.setEditFlag(true);
        post.setDescription(newPost.getDescription());
        post.setPostDetails(newPost.getPostDetails());
        post.setAddress(newPost.getAddress());
        post.setCity(newPost.getCity());
        post.setParking(newPost.getParking());
        post.setOpeningHour(newPost.getOpeningHour());
        post.setClosingHour(newPost.getClosingHour());
        post.setPhoneNumber(newPost.getPhoneNumber());
        post.setModifiedDate(currentTime);

        post.setAlias(newPost.getAlias());

        post.setLatitude(newPost.getLatitude());
        post.setLongitude(newPost.getLongitude());

        //remove old Files
        List<PlaceDetails> oldDetails = postDetailRepository.findByPost(post);
        for(PlaceDetails i : oldDetails){ 
            fileService.deleteFile(FILE_URL+i.getFileName());
        }
        postDetailRepository.deleteAll(oldDetails);

        //add new Files
        List<PlaceDetails> newPostDetails = new ArrayList<>();
        Arrays.asList(files).stream().forEach(file-> {
            PlaceDetails postDetail = new PlaceDetails();
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

    //Function to convert Post List -> Post List Dto 
    public List<PostDto> convertToDto(List<Place> post){
        List<PostDto> postDtoList = new ArrayList<PostDto>();

        for(Place i : post){
            float rating = 0, rounded = 0;
            Integer totalRating = reviewRepository.getPostTotalRating(i.getId());
            Integer totalRatingThisMonth = reviewRepository.getPostTotalRatingThisMonth(i.getId());
            if(totalRating > 0){
                rating = reviewRepository.getPostAverageRating(i.getId());
            }
            rounded = Math.round(rating*100)/100.f;
            postDtoList.add(new PostDto(i.getId(), 
                i.getTitle(), 
                i.getDescription(), 
                (i.getPostDetails().size() == 0 ? 
                    "Not Found" : 
                    i.getPostDetails().get(0).getUrl()+
                    i.getPostDetails().get(0).getFileName()),
                i.getCategory().getCategory(),
                i.getCity(),
                i.getCreatedDate(),
                rounded, 
                totalRating,
                totalRatingThisMonth,
                i.getLatitude(),
                i.getLongitude(),
                0
                )
            );
        }
        return postDtoList;
    }

    public Page<PostDto> search(SortDto sortDto, String sortBy, String sortDir, int page, int size){
        List<Place> posts = postRepository.findAll(PlaceSpecification.findBySpecification(sortDto));

        List<PostDto> postDtoList = convertToDto(posts);

        if(sortBy.equalsIgnoreCase("createdDate") || sortBy.isEmpty()){
            Collections.sort(postDtoList, new Comparator<PostDto>() {
                public int compare(PostDto a, PostDto b){
                    return a.getCreatedDate().compareTo(b.getCreatedDate());
                } 
            });
        }
        else if(sortBy.equalsIgnoreCase("rating")){
            Collections.sort(postDtoList, (a,b)-> a.getTotalRating()-b.getTotalRating());
            Collections.sort(postDtoList, new Comparator<PostDto>() {
                public int compare(PostDto a, PostDto b){
                    return Float.compare(a.getAverageRating(), b.getAverageRating());
                } 
            });
        }
        else if(sortBy.equalsIgnoreCase("trending")){
            postDtoList.sort(Comparator.comparingInt(PostDto::getTotalRatingThisMonth));
        }
        else if(sortBy.equalsIgnoreCase("reviews")){
            Collections.sort(postDtoList, new Comparator<PostDto>() {
                public int compare(PostDto a, PostDto b){
                    return Float.compare(a.getAverageRating(), b.getAverageRating());
                } 
            });
            Collections.sort(postDtoList, (a,b)-> a.getTotalRating()-b.getTotalRating());
        }
        else if(sortBy.equalsIgnoreCase("nearest")){
            for(PostDto i : postDtoList){
                double distance = geolocationService.calculateDistance(
                    new GeolocationRequest("", sortDto.getLongtitude(), sortDto.getLatitude()), i.getLatitude(), i.getLongtitude());
                i.setDistance(distance);
            }
            Collections.sort(postDtoList, Comparator.comparingDouble(PostDto::getDistance));
        }
        else if(sortBy.equalsIgnoreCase("alphabet")){
            Collections.sort(postDtoList, Comparator.comparing(PostDto::getTitle));
        }

        if(!sortDir.equalsIgnoreCase("ASC")){
            Collections.reverse(postDtoList);
        }

        int start = page*size;
        int end = Math.min(start+size, postDtoList.size());
        if(start > postDtoList.size()) {
            return new PageImpl<>(List.of()); 
        }

        List<PostDto> subList = postDtoList.subList(start, end);
        return new PageImpl<>(subList, PageRequest.of(page,size), postDtoList.size());
    }

    public Place findById(Integer id) throws ErrorMessage{
        Optional<Place> post = postRepository.findById(id);
        if(post.isPresent()){
            return post.get();
        }
        throw new ErrorMessage("Post Not Found");
    }
    
    //find Nearest by Location
    public List<PostDto> findNearest(User user, GeolocationRequest geolocationRequest){
        List<PostDto> postList = this.getAllPostsDto();
        geolocationService.setUserLocation(user, geolocationRequest);

        for(PostDto i : postList){
            double distance = geolocationService.calculateDistance(geolocationRequest, i.getLatitude(), i.getLongtitude());
            i.setDistance(distance);
        }

        Collections.sort(postList, Comparator.comparingDouble(PostDto::getDistance));
        return postList;
    }

    public List<PostDto> getMostRatingThisMonth(Integer limit){
        List<PostDto> postList = this.getAllPostsDto();
        postList.sort(Comparator.comparingInt(PostDto::getTotalRatingThisMonth));
        return postList.subList(0, limit);
    }

    public List<PostDto> getAllPostsDto(){
        List<Place> posts = postRepository.findAll();
        return convertToDto(posts);
    }

    public Page<PostDto> getPostByUser(User user, Pageable pageable){
        Page<Place> posts = postRepository.findByUser(user, pageable);
        List<PostDto> postDtoList = convertToDto(posts.toList());

        int page = pageable.getPageNumber();
        int size = pageable.getPageSize();

        int start = page*size;
        int end = Math.min(start+size, postDtoList.size());
        if(start > postDtoList.size()) {
            return new PageImpl<>(List.of()); 
        }

        List<PostDto> subList = postDtoList.subList(start, end);
        return new PageImpl<>(subList, PageRequest.of(page,size), postDtoList.size());

    }

    public List<Resource> getFiles(Integer postId){
        List<Resource> resources = new ArrayList<>();
        Optional<Place> post = postRepository.findById(postId);
        if(post.isPresent()){
            for(PlaceDetails postDetails : post.get().getPostDetails()){
                resources.add(fileService.getFileByName(postDetails.getFileName(), postDetails.getUrl()));
            }
        }
        return resources;
    }

    public Boolean deletePost(User user, Integer postId) throws ErrorMessage{
        Place post = this.findById(postId);

        if(post.getUser().equals(user)){

            //delete claims
            List<Claim> claims = claimRepository.findByPost(post);
            claimRepository.deleteAll(claims);
            
            //delete reports
            List<Report> reports = reportRepository.findByPost(post);
            reportRepository.deleteAll(reports);

            //delete details
            List<PlaceDetails> postDetails = postDetailRepository.findByPost(post);
            for(PlaceDetails i : postDetails){
                fileService.deleteFile(FILE_URL+i.getFileName());
            }
            postDetailRepository.deleteAll(postDetails);
            postRepository.delete(post);  
            
            return true; 
        }

        
        throw new ErrorMessage("Post Belongs To Another User");
    }

    public Place claimPost(User user, Place post){
        post.setUser(user);
        post.setVerified(true);
        post.setModifiedDate(LocalDateTime.now());
        return postRepository.save(post);
    }


    //ADMIN ONLY
    public void deletePostByAdmin(Integer id) throws ErrorMessage{
        //send Notification to user
        Place post = postRepository.findById(id).get();
        if(post != null){
            deletePost(post.getUser(), id);
            return;
        }
        throw new ErrorMessage("Failed to delete Post");
    }



}
