package com.TravelApp.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.TravelApp.entity.Post;

import jakarta.persistence.criteria.Predicate;

public class PostSpecification {
    
    public static Specification<Post> findBySpecification(Post postSearch){
        return ((root, query, critriaBuilder) -> {
            List<Predicate> predicate = new ArrayList<>();

            if(postSearch.getCategory() != null){
                predicate.add(critriaBuilder.equal(root.get("category"), postSearch.getCategory()));
            }

            if(postSearch.getTitle() != null && !postSearch.getTitle().isEmpty()){
                predicate.add(critriaBuilder.like(root.get("title"), "%"+postSearch.getTitle()+"%"));
            }

            if(postSearch.getCity() != null && !postSearch.getCity().isEmpty()){
                predicate.add(critriaBuilder.like(root.get("city"), "%"+postSearch.getCity()+"%"));
            }

            return critriaBuilder.and(predicate.toArray(new Predicate[0]));
        }
        );
    }

}
