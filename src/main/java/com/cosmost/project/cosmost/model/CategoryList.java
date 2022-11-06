package com.cosmost.project.cosmost.model;

import com.cosmost.project.cosmost.infrastructure.entity.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;



@Getter
@ToString
@Builder
@AllArgsConstructor
public class CategoryList {

    private Long id;

    private String locationCategoryName;
    private String themeCategoryName;

    public CategoryList(CategoryListEntity categoryListEntity) {
        this.id = categoryListEntity.getId();
        this.locationCategoryName = categoryListEntity.getLocationCategory().getLocationCategoryName();
        this.themeCategoryName = categoryListEntity.getThemeCategory().getThemeCategoryName();
    }

}
