package com.TravelApp.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.TravelApp.dto.SortDto;

import jakarta.persistence.criteria.Predicate;

public class PostSpecification {
    
    public static Specification<SortDto> findBySpecification(SortDto postSearch){
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicate = new ArrayList<>();

            if(postSearch.getCategories() != null){
                predicate.add(criteriaBuilder.in(root.get("category")).value(postSearch.getCategories()));
            }

            if(postSearch.getTitle() != null && !postSearch.getTitle().isEmpty()){
                Predicate titlePredicate = criteriaBuilder.like(root.get("title"), "%"+postSearch.getTitle()+"%");
                Predicate aliasPredicaate = criteriaBuilder.like(root.get("alias"), "%"+postSearch.getTitle()+"%");
                predicate.add(criteriaBuilder.or(titlePredicate, aliasPredicaate));
            }

            if(postSearch.getCities() != null && !postSearch.getCities().isEmpty()){
                predicate.add(criteriaBuilder.in(root.get("city")).value(postSearch.getCities()));
            }

            return criteriaBuilder.and(predicate.toArray(new Predicate[0]));
        }
        );
    }

}
