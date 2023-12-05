package com.TravelApp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.TravelApp.entity.Category;
import com.TravelApp.entity.Claim;
import com.TravelApp.entity.Report;
import com.TravelApp.service.CategoryService;
import com.TravelApp.service.ClaimService;
import com.TravelApp.service.FileService;
import com.TravelApp.service.PostService;
import com.TravelApp.service.ReportService;
import com.TravelApp.util.ErrorMessage;


@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private PostService postService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private ClaimService claimService;

    @Autowired
    private FileService fileService;

    //POST
    @DeleteMapping("/delete/{id}")
    public String deletePost(@PathVariable("id") Integer id) throws ErrorMessage{
        return postService.deletePostByAdmin(id);
    }

    //NOT TESTED
    @PostMapping("/createCategory")
    public Category createCategory(@RequestBody Category category){
        return categoryService.createCategory(category);
    }

    //Get All User Reports
    @GetMapping("/getUserReports")
    public List<Report> getUserReports(){
        return reportService.getAll();
    }

    //View, Approve, Reject Claims
    @GetMapping("/claim/getAll")
    public List<Claim> getAllClaims(){
        return claimService.getAllClaims();
    }

    @GetMapping("/claim/approve/{id}")
    public Claim approveClaim(@PathVariable("id") Integer id){
        return claimService.approveClaim(id);
    }

    @GetMapping("claim/reject/{id}")
    public Claim rejectClaim(@PathVariable("id") Integer id){
        return claimService.rejectClaim(id);
    }

    //Send chat to user



    //WARNING
    @GetMapping("flush-all")
    public void flushFiles(){
        fileService.flushAllFiles();
    }



    
}
