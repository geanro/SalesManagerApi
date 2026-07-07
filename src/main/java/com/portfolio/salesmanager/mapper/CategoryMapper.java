package com.portfolio.salesmanager.mapper;

import com.portfolio.salesmanager.dto.request.CategoryRequest;
import com.portfolio.salesmanager.dto.response.CategoryResponse;
import com.portfolio.salesmanager.entity.Category;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryMapper {

    public CategoryResponse toResponse(Category category){
        if (category==null) return null;

        return CategoryResponse.builder()
                .idCategory(category.getIdCategory())
                .name(category.getName())
                .description(category.getDescription())
                .active(category.getActive())
                .build();
    }

    public List<CategoryResponse> toResponseList(List<Category> categories){
        if (categories==null) return List.of();

        return categories.stream()
                .map(this::toResponse)
                .toList();
    }

    public Category toEntity(CategoryRequest categoryRequest){
        if (categoryRequest==null) return null;

        return Category.builder()
                .name(categoryRequest.getName())
                .description(categoryRequest.getDescription())
                .active(categoryRequest.getActive() !=null? categoryRequest.getActive():true)
                .build();
    }
}
