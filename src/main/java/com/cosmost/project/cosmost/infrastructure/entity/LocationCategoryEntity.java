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
@Table(name = "location_category")
public class LocationCategoryEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String locationCategoryName;


    @Builder
    public LocationCategoryEntity(Long id, String locationCategoryName) {
        this.id = id;
        this.locationCategoryName = locationCategoryName;
    }

}
