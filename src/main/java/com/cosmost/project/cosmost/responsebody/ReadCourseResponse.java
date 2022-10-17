package com.cosmost.project.cosmost.responsebody;

import com.cosmost.project.cosmost.infrastructure.entity.CourseStatus;
import com.cosmost.project.cosmost.model.PlaceDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReadCourseResponse {

    private Long id;
    private Long authorId;
    private String courseTitle;
    private CourseStatus courseStatus;

    private List<ReadPlaceDetailResponse> readPlaceDetailResponseList;

}
