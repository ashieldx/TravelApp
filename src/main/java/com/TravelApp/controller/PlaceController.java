package com.TravelApp.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

import com.TravelApp.dto.GeolocationRequest;
import com.TravelApp.dto.PostDto;
import com.TravelApp.dto.ReportDto;
import com.TravelApp.dto.SortDto;
import com.TravelApp.entity.Claim;
import com.TravelApp.entity.Place;
import com.TravelApp.entity.Report;
import com.TravelApp.entity.User;
import com.TravelApp.response.CommonResponse;
import com.TravelApp.response.CommonResponseGenerator;
import com.TravelApp.service.ClaimService;
import com.TravelApp.service.FileService;
import com.TravelApp.service.PlaceService;
import com.TravelApp.service.ReportService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/post")
public class PlaceController {

    @Autowired
    private PlaceService placeService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private ClaimService claimService;

    @Autowired
    private FileService fileService;

    @Autowired
    private CommonResponseGenerator commonResponseGenerator;

    // Post Entity
    @PostMapping("/create")
    public CommonResponse<Place> createPost(@AuthenticationPrincipal User user, @RequestParam("files") MultipartFile[] files,
        @RequestParam("data") String postString) throws JsonMappingException, JsonProcessingException{
        Place post = new ObjectMapper().readValue(postString, Place.class);
        Place postResponse = null;
        try{
            fileService.validateFiles(files);
            postResponse = placeService.savePost(user, post, files);
        } catch(Exception e){
            return commonResponseGenerator.errorResponse(null, e.getMessage());
        }
        return commonResponseGenerator.successResponse(postResponse, "Create Post Success");
    }

    @PostMapping("report/{id}")
    public CommonResponse<Report> createReport(@AuthenticationPrincipal User user, @PathVariable("id") Integer id, @RequestBody ReportDto report){
        Report reportResponse = null;
        try{
            reportResponse = reportService.createReport(user, id, report.getMessage(), report.getType());
        }catch(Exception e){
            return commonResponseGenerator.errorResponse(null, e.getMessage());
        }
        return commonResponseGenerator.successResponse(reportResponse, "Report Success");
    }

    @PostMapping("/claim/{id}")
    public CommonResponse<Claim> claimPost(@AuthenticationPrincipal User user, @PathVariable("id") Integer id, @RequestParam("files") MultipartFile[] files,
        @RequestParam("data") String claimString) throws JsonMappingException, JsonProcessingException{  
        Claim claim = new ObjectMapper().readValue(claimString, Claim.class);
        Claim claimResponse = null;

        String desc = claim.getDescription();
        try{
            claimResponse = claimService.claimPost(user, desc, id, files);
        }catch (Exception e){
            return commonResponseGenerator.errorResponse(null, e.getMessage());
        }
        return commonResponseGenerator.successResponse(claimResponse, "Request Claim Success");
    }

    @PutMapping("/edit/{id}")
    public CommonResponse<Place> editPost(@AuthenticationPrincipal User user, @RequestParam("files") MultipartFile[] files,
         @PathVariable("id") Integer id, @RequestParam("data") String postString) throws JsonMappingException, JsonProcessingException{
        Place post = new ObjectMapper().readValue(postString, Place.class);
        Place postResponse = null;
        try{
            fileService.validateFiles(files);
            postResponse = placeService.editPost(user, id, post, files);
        }catch (Exception e){
            return commonResponseGenerator.errorResponse(null, e.getMessage());
        }
        return commonResponseGenerator.successResponse(postResponse, "Edit Post Success");
    }

    @DeleteMapping("/delete/{id}")
    public CommonResponse<String> deletePost(@AuthenticationPrincipal User user, @PathVariable("id") Integer id){
        try{
            placeService.deletePost(user, id);
        } catch(Exception e){
            return commonResponseGenerator.errorResponse(null, e.getMessage());
        }
        return commonResponseGenerator.successResponse("Post Deleted", "Delete Post Success");
    }

    @GetMapping("/get/{id}")
    public CommonResponse<Place> getPostById(@PathVariable("id") Integer id){
        Place postResponse = null;
        try{
            postResponse = placeService.findById(id);
        } catch(Exception e){
            return commonResponseGenerator.errorResponse(null, e.getMessage());
        }
        return commonResponseGenerator.successResponse(postResponse, "Get Post Success");
    }
    
    @GetMapping("/getAll")
    public CommonResponse<List<PostDto>> getAll(){

        List<PostDto> postResponse = null;
        try{
            postResponse = placeService.getAllPostsDto();
        }catch(Exception e){
            return commonResponseGenerator.errorResponse(null, e.getMessage());
        }
        return commonResponseGenerator.successResponse(postResponse, "Get Post List Success");
    }

    @PostMapping("/search")
    public CommonResponse<Page<PostDto>> search(@RequestBody(required = false) SortDto sortDto, @RequestParam String sortBy, @RequestParam String sortDir,
        @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "10") int size){
        Page<PostDto> postResponse = null;
        try{      
            postResponse = placeService.search(sortDto, sortBy, sortDir, page, size);
        }catch (Exception e){
            return commonResponseGenerator.errorResponse(null, "Search By Criteria Error");
        }
        return commonResponseGenerator.successResponse(postResponse, "Search by Criteria Success");
    }

    //find Nearest location
    @PostMapping("/findNearest")
    public CommonResponse<List<PostDto>> findNearest(@AuthenticationPrincipal User user, @RequestBody GeolocationRequest geolocationRequest){
        List<PostDto> postResponse = null;
        try{
            postResponse = placeService.findNearest(user, geolocationRequest);
        }catch(Exception e){
            return commonResponseGenerator.errorResponse(null, "Failed to Find Nearest Place");
        }
        return commonResponseGenerator.successResponse(postResponse, "Find Nearest Place Success");
    }


    //Most Reviews this Month
    @GetMapping("/getTrending/{limit}")
    public CommonResponse<List<PostDto>> getTrending(@PathVariable(name = "limit") Optional<Integer> limit) {
        int actualLimit = limit.orElse(8);
        List<PostDto> postResponse = null;
        try{
            postResponse = placeService.getMostRatingThisMonth(actualLimit);
        }catch(Exception e){
            return commonResponseGenerator.errorResponse(null, e.getMessage());
        }
        return commonResponseGenerator.successResponse(postResponse, "Get Top Rated This Month Success");
    }

    @DeleteMapping("/admin/delete/{id}")
    public CommonResponse<String> deletePost(@PathVariable("id") Integer id){
        try{
            placeService.deletePostByAdmin(id);
        }catch(Exception e){
            return commonResponseGenerator.errorResponse(null, e.getMessage());
        }
        return commonResponseGenerator.successResponse("Post Deleted", "Delete Post by Admin Success");
    }
    

}   

