package com.TravelApp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.TravelApp.dto.RequestDto;
import com.TravelApp.entity.Claim;
import com.TravelApp.entity.Report;
import com.TravelApp.response.CommonResponse;
import com.TravelApp.response.CommonResponseGenerator;
import com.TravelApp.service.ClaimService;
import com.TravelApp.service.FileService;
import com.TravelApp.service.ReportService;


@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private ClaimService claimService;

    @Autowired
    private FileService fileService;

    @Autowired
    private CommonResponseGenerator commonResponseGenerator;

    //Reports
    @GetMapping("/getUserReports")
    public CommonResponse<List<Report>> getUserReports(){
        List<Report> reportResponse = null;
        try{
            reportResponse = reportService.getAll();
        }catch (Exception e){
            return commonResponseGenerator.errorResponse(null, "Failed to Retrieve Reports");
        }
       return commonResponseGenerator.errorResponse(reportResponse, "Get Report List Success");
    }

    //View, Approve, Reject Claims
    @PostMapping("/claim/getAll")
    public CommonResponse<List<Claim>> getAllClaims(@RequestBody RequestDto requestDto){
        List<Claim> claimResponse = null;
        try{
            claimResponse = claimService.getAllClaims(requestDto.getString());
        } catch(Exception e){
            return commonResponseGenerator.errorResponse(null , "Failed to Retrieve Claims");
        }
        return commonResponseGenerator.successResponse(claimResponse, "Get All Claims Success");
    }

    @GetMapping("/claim/approve/{id}")
    public CommonResponse<Claim> approveClaim(@PathVariable("id") Integer id){
        Claim claimResponse = null;
        try{
            claimResponse = claimService.approveClaim(id);
        }catch(Exception e){
            return commonResponseGenerator.errorResponse(null, "Error Approve Claim");
        }
        return commonResponseGenerator.successResponse(claimResponse, "Approve Claim Success");
    }

    @PostMapping("claim/reject/{id}")
    public CommonResponse<Claim> rejectClaim(@PathVariable("id") Integer id, @RequestBody RequestDto requestDto){
        Claim claimResponse = null;
        try{
            claimResponse = claimService.rejectClaim(id, requestDto.getString());
        }catch(Exception e){
            return commonResponseGenerator.errorResponse(null, "Error Reject Claim");
        }
        return commonResponseGenerator.successResponse(claimResponse, "Reject Claim Success");
    }

    //Send chat to user



    //WARNING
    @GetMapping("flush-all")
    public CommonResponse<String> flushFiles(){
        try{
            fileService.flushAllFiles();
        }catch(Exception e){
            return commonResponseGenerator.errorResponse(null, "Error Flush Files");
        }
        return commonResponseGenerator.successResponse(null, "Flush Files Success");
    }



    
}
