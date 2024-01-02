package com.TravelApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.TravelApp.service.FileService;


@RestController
@RequestMapping("/uploads")
public class FileController {

    @Autowired
    private FileService fileService;

    @GetMapping("/{path}/{filename}")
    public Resource getFile(@PathVariable("path") String path, @PathVariable("filename") String filename){
        return fileService.getFileByName(filename, path);
    }
    
}
