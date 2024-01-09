package com.TravelApp.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.TravelApp.entity.Post;
import com.TravelApp.entity.Report;
import com.TravelApp.entity.User;
import com.TravelApp.repository.ReportRepository;
import com.TravelApp.util.ErrorMessage;

@Service
public class ReportService {

    @Autowired
    private PostService postService;

    @Autowired
    private ReportRepository reportRepository;

    //get all reports (admin)
    public Page<Report> getAll(Pageable pageable){
        return reportRepository.findAll(pageable);
    }

    public Report createReport(User user, Integer postId, String message, String type) throws ErrorMessage{
        Report report = new Report();
        Post post  = postService.findById(postId);
        
        report.setType(type);
        report.setSenderId(user.getId());
        report.setPost(post);
        report.setMessage(message);
        report.setCreatedDate(LocalDateTime.now());

        return reportRepository.save(report);
    }

    public Report processReport(Integer reportId){
        Report report = reportRepository.findById(reportId).get();
        report.setProcessed(true);
        return reportRepository.save(report);
    }

    
    
}
