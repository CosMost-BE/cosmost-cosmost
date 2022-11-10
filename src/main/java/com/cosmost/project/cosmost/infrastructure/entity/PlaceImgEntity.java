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
@Table(name = "place_img")
public class PlaceImgEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String placeImgOriginName;

    @NotNull
    private String placeImgSaveName;

    @NotNull
    private String placeImgUrl;

    @NotNull
    private int placeImgOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    private CourseEntity course;

    @Builder
    public PlaceImgEntity(Long id, String placeImgOriginName, String placeImgSaveName,
                          String placeImgUrl, int placeImgOrder, CourseEntity course) {
        this.id = id;
        this.placeImgOriginName = placeImgOriginName;
        this.placeImgSaveName = placeImgSaveName;
        this.placeImgUrl = placeImgUrl;
        this.placeImgOrder = placeImgOrder;
        this.course = course;
    }

}
