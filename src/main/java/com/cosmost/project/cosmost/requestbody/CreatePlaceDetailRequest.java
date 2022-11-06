package com.cosmost.project.cosmost.requestbody;

import com.cosmost.project.cosmost.infrastructure.entity.CourseEntity;
import com.cosmost.project.cosmost.infrastructure.entity.PlaceDetailEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatePlaceDetailRequest {

    @NotBlank(message = "장소 이름은 필수 입력 값입니다.")
    private String placeName;

    private double placeXCoordinate;
    private double placeYCoordinate;
    private int placeOrder;

    @NotBlank(message = "장소 후기는 필수 입력 값입니다.")
    private String placeComment;

    private Long course;

    public PlaceDetailEntity createDtoToEntity(CreatePlaceDetailRequest createPlaceDetailRequest, CourseEntity courseEntity) {
        return PlaceDetailEntity.builder()
                .placeName(createPlaceDetailRequest.getPlaceName())
                .placeXCoordinate(createPlaceDetailRequest.getPlaceXCoordinate())
                .placeYCoordinate(createPlaceDetailRequest.getPlaceYCoordinate())
                .placeOrder(createPlaceDetailRequest.getPlaceOrder())
                .placeComment(createPlaceDetailRequest.getPlaceComment())
                .course(courseEntity)
                .build();
    }
}

