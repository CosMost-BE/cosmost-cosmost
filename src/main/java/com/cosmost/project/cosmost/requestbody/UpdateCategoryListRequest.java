package com.cosmost.project.cosmost.requestbody;

import com.cosmost.project.cosmost.infrastructure.entity.CategoryListEntity;
import com.cosmost.project.cosmost.infrastructure.entity.CourseEntity;
import com.cosmost.project.cosmost.infrastructure.entity.LocationCategoryEntity;
import com.cosmost.project.cosmost.infrastructure.entity.ThemeCategoryEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateCategoryListRequest {
    private Long id;

    private Long course;
    private Long locationCategory;
    private Long themeCategory;

    public CategoryListEntity updateDtoToEntity(CourseEntity courseEntity, LocationCategoryEntity locationCategoryEntity,
                                                ThemeCategoryEntity themeCategoryEntity, UpdateCategoryListRequest updateCategoryListRequest) {
        return CategoryListEntity.builder()
                .id(updateCategoryListRequest.getId())
                .course(courseEntity)
                .locationCategory(locationCategoryEntity)
                .themeCategory(themeCategoryEntity)
                .build();
    }
}

