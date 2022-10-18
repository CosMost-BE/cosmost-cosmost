package com.cosmost.project.cosmost.model;

import com.cosmost.project.cosmost.infrastructure.entity.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.FetchType;
import javax.persistence.ManyToOne;


@Getter
@ToString
@Builder
@AllArgsConstructor
public class CategoryList {

    private Long id;

    private Long course;
    private Long locationCategory;
    private Long themeCategory;

    public CategoryList(CategoryListEntity categoryListEntity) {
        this.id = categoryListEntity.getId();
        this.course = categoryListEntity.getCourse().getId();
        this.locationCategory = categoryListEntity.getLocationCategory().getId();
        this.themeCategory = categoryListEntity.getThemeCategory().getId();

    }

}
