package com.cosmost.project.cosmost.responsebody;

import com.cosmost.project.cosmost.infrastructure.entity.CourseStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReadPlaceDetailResponse {

    private Long id;

    private String placeName;
    private String placeUrl;
    private int placeOrder;
    private String placeComment;

    private Long course;

}
