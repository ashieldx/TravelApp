package com.TravelApp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.TravelApp.entity.Post;
import com.TravelApp.entity.Report;

public interface ReportRepository extends JpaRepository<Report, Integer>{

    List<Report> findByPost(Post post);

}
