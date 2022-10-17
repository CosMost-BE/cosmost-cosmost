package com.cosmost.project.cosmost.model;

import com.cosmost.project.cosmost.infrastructure.entity.PlaceDetailEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;


@Getter
@ToString
@Builder
@AllArgsConstructor
public class PlaceDetail {

    private Long id;

    private String placeName;
    private String placeUrl;
    private int placeOrder;
    private String placeComment;

//    private Long course;

    public PlaceDetail(PlaceDetailEntity placeDetailEntity) {
        this.id = placeDetailEntity.getId();
        this.placeName = placeDetailEntity.getPlaceName();
        this.placeUrl = placeDetailEntity.getPlaceUrl();
        this.placeOrder = placeDetailEntity.getPlaceOrder();
        this.placeComment = placeDetailEntity.getPlaceComment();
    }

}
