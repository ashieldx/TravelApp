package com.TravelApp.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.TravelApp.dto.SortDto;

import jakarta.persistence.criteria.Predicate;

public class PlaceSpecification {
    
    public static Specification<SortDto> findBySpecification(SortDto place){
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicate = new ArrayList<>();

            if(place.getCategories() != null){
                predicate.add(criteriaBuilder.in(root.get("category")).value(place.getCategories()));
            }

            if(place.getTitle() != null && !place.getTitle().isEmpty()){
                Predicate titlePredicate = criteriaBuilder.like(root.get("title"), "%"+place.getTitle()+"%");
                Predicate aliasPredicaate = criteriaBuilder.like(root.get("alias"), "%"+place.getTitle()+"%");
                predicate.add(criteriaBuilder.or(titlePredicate, aliasPredicaate));
            }

            if(place.getCities() != null && !place.getCities().isEmpty()){
                predicate.add(criteriaBuilder.in(root.get("city")).value(place.getCities()));
            }

            return criteriaBuilder.and(predicate.toArray(new Predicate[0]));
        }
        );
    }

}
