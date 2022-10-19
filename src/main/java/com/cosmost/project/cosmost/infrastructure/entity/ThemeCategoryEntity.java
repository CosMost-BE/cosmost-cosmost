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
@Table(name = "theme_category")
public class ThemeCategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String themeCategoryName;


    @Builder
    public ThemeCategoryEntity(Long id, String themeCategoryName) {
        this.id = id;
        this.themeCategoryName = themeCategoryName;
    }

}
