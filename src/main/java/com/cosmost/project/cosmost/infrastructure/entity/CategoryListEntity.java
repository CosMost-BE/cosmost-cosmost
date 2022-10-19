package com.cosmost.project.cosmost.infrastructure.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "category_list")
public class CategoryListEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private CourseEntity course;

    @ManyToOne(fetch = FetchType.LAZY)
    private LocationCategoryEntity locationCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    private ThemeCategoryEntity themeCategory;

    @Builder
    public CategoryListEntity(Long id, CourseEntity course, LocationCategoryEntity locationCategory,
                              ThemeCategoryEntity themeCategory) {
        this.id = id;
        this.course = course;
        this.locationCategory = locationCategory;
        this.themeCategory = themeCategory;
    }

}
