package com.cosmost.project.cosmost.model;

import com.cosmost.project.cosmost.infrastructure.entity.PlaceDetailEntity;
import com.cosmost.project.cosmost.infrastructure.entity.PlaceImgEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;


@Getter
@ToString
@Builder
@AllArgsConstructor
public class PlaceImg {

    private Long id;

    private String placeImgOriginName;
    private String placeImgSaveName;
    private String placeImgUrl;
    private int placeImgOrder;


    public PlaceImg(PlaceImgEntity placeImgEntity) {
        this.id = placeImgEntity.getId();
        this.placeImgOriginName = placeImgEntity.getPlaceImgOriginName();
        this.placeImgSaveName = placeImgEntity.getPlaceImgSaveName();
        this.placeImgUrl = placeImgEntity.getPlaceImgUrl();
        this.placeImgOrder = placeImgEntity.getPlaceImgOrder();

    }

}
