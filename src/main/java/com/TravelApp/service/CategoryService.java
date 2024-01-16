package com.TravelApp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.TravelApp.entity.Category;
import com.TravelApp.repository.CategoryRepository;

@Service
public class CategoryService {  
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAll(){
        Sort sort = Sort.by("category").ascending();
        return categoryRepository.findAll(sort);
    }

    public Category createCategory(Category category){
        return categoryRepository.save(category);
    }
    
}
