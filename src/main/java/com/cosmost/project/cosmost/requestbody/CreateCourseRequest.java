package com.cosmost.project.cosmost.requestbody;

import com.cosmost.project.cosmost.infrastructure.entity.CourseEntity;
import com.cosmost.project.cosmost.infrastructure.entity.CourseStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCourseRequest {

    private Long authorId;

    @NotBlank(message = "코스 제목은 필수 입력 값입니다.")
    private String courseTitle;

    @NotBlank(message = "코스 후기는 필수 입력 값입니다.")
    private String courseComment;

    private CourseStatus courseStatus;

    @Valid
    private List<CreatePlaceDetailRequest> createPlaceDetailRequestList;

    @Valid
    private List<CreateHashtagRequest> createHashtagRequestList;

    private List<CreatePlaceImgRequest> createPlaceImgRequestList;

    private List<CreateCategoryListRequest> createCategoryListRequestList;

    public CourseEntity createDtoToEntity(CreateCourseRequest createCourseRequest, Long authorId) {
        return CourseEntity.builder()
                .authorId(authorId)
                .courseTitle(createCourseRequest.getCourseTitle())
                .courseComment(createCourseRequest.getCourseComment())
                .courseStatus(CourseStatus.ACTIVE)
                .build();
    }
}

