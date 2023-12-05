package com.TravelApp.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.TravelApp.entity.Report;
import com.TravelApp.entity.User;
import com.TravelApp.repository.ReportRepository;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    //get all reports (admin)
    public List<Report> getAll(){
        return reportRepository.findAll();
    }

    public Report createReport(User user, Integer postId, String message){
        Report report = new Report();
        report.setSenderId(user.getId());
        report.setPostId(postId);
        report.setMessage(message);
        report.setCreatedDate(LocalDateTime.now());

        return reportRepository.save(report);
    }

    
    
}
