package com.TravelApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    @GetMapping("/report/get")
    public CommonResponse<Page<Report>> getUserReports(
        @RequestParam(name = "status", defaultValue =  "ALL") String status,
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue =  "8") int size
    ){
        Page<Report> reportResponse = null;
        try{
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
            reportResponse = reportService.getAll(pageable);
        }catch (Exception e){
            return commonResponseGenerator.errorResponse(null, "Failed to Retrieve Reports");
        }
       return commonResponseGenerator.successResponse(reportResponse, "Get Report List Success");
    }

    @GetMapping("/report/process/{id}")
    public CommonResponse<Report> processReport(@PathVariable("id") Integer id){
        Report reportResponse = null;
        try{
            reportResponse = reportService.processReport(id);
        }catch(Exception e){
            return commonResponseGenerator.errorResponse(null, "Failed to Process Report");
        }
        return commonResponseGenerator.successResponse(reportResponse, "Process Report Success");
    }

    //View, Approve, Reject Claims
    @GetMapping("/claim/getAll")
    public CommonResponse<Page<Claim>> getAllClaims(
        @RequestParam(name = "status", defaultValue =  "ALL") String status,
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue =  "8") int size
        )
    {
        Page<Claim> claimResponse = null;
        try{
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
            claimResponse = claimService.getAllClaims(pageable, status);
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
