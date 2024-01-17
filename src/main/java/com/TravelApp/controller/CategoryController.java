package com.TravelApp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.TravelApp.entity.Category;
import com.TravelApp.response.CommonResponse;
import com.TravelApp.response.CommonResponseGenerator;
import com.TravelApp.service.CategoryService;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CommonResponseGenerator commonResponseGenerator;

    @GetMapping("/getAll")
    public CommonResponse<List<Category>> getAll(){
        return commonResponseGenerator.successResponse(categoryService.getAll(), "Get Category List Success");
    }

    @PostMapping("/admin/create")
    public CommonResponse<Category> createCategory(@RequestBody Category category){
        Category categoryResponse = null;
        try{
            categoryResponse = categoryService.createCategory(category);;
        }catch (Exception e){
            return commonResponseGenerator.successResponse(null , "Failed to Create Category");
        }
        return commonResponseGenerator.errorResponse(categoryResponse, "Create Category Success");
    }

}
