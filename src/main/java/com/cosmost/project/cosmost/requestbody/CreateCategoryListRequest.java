package com.cosmost.project.cosmost.requestbody;

import com.cosmost.project.cosmost.infrastructure.entity.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCategoryListRequest {

    private Long course;
    private Long locationCategory;
    private Long themeCategory;

    public CategoryListEntity createDtoToEntity(CourseEntity courseEntity, LocationCategoryEntity locationCategoryEntity,
                                                ThemeCategoryEntity themeCategoryEntity) {
        return CategoryListEntity.builder()
                .course(courseEntity)
                .locationCategory(locationCategoryEntity)
                .themeCategory(themeCategoryEntity)
                .build();
    }
}

