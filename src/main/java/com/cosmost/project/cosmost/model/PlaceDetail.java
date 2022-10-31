package com.cosmost.project.cosmost.model;

import com.cosmost.project.cosmost.infrastructure.entity.PlaceDetailEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;


@Getter
@ToString
@Builder
@AllArgsConstructor
public class PlaceDetail {

    private Long id;

    private String placeName;
    private double placeXCoordinate;
    private double placeYCoordinate;
    private int placeOrder;
    private String placeComment;

    public PlaceDetail(PlaceDetailEntity placeDetailEntity) {
        this.id = placeDetailEntity.getId();
        this.placeName = placeDetailEntity.getPlaceName();
        this.placeXCoordinate = placeDetailEntity.getPlaceXCoordinate();
        this.placeYCoordinate = placeDetailEntity.getPlaceYCoordinate();
        this.placeOrder = placeDetailEntity.getPlaceOrder();
        this.placeComment = placeDetailEntity.getPlaceComment();
    }

}
