package com.cosmost.project.cosmost.requestbody;

import com.cosmost.project.cosmost.infrastructure.entity.CourseEntity;
import com.cosmost.project.cosmost.infrastructure.entity.PlaceDetailEntity;
import com.cosmost.project.cosmost.infrastructure.entity.PlaceImgEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatePlaceImgRequest {

    @NotBlank(message = "장소 이미지 원본 이름은 필수 입력 값입니다.")
    private String placeImgOriginName;

    @NotBlank(message = "장소 이미지 저장 이름은 필수 입력 값입니다.")
    private String placeImgSaveName;

    @NotBlank(message = "장소 이미지 저장 URL은 필수 입력 값입니다.")
    private String placeImgUrl;

    private Long course;

    public PlaceImgEntity createDtoToEntity(CreatePlaceImgRequest createPlaceImgRequest, CourseEntity courseEntity) {
        return PlaceImgEntity.builder()
                .placeImgOriginName(createPlaceImgRequest.getPlaceImgOriginName())
                .placeImgSaveName(createPlaceImgRequest.getPlaceImgSaveName())
                .placeImgUrl(createPlaceImgRequest.getPlaceImgUrl())
                .course(courseEntity)
                .build();
    }
}

