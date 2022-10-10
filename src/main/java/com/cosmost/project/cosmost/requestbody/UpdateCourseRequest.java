package com.cosmost.project.cosmost.requestbody;

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
public class UpdateCourseRequest {

    @NotBlank(message = "코스 PK는 필수 입력 값입니다.")
    private Long id;

    @NotBlank(message = "작성자 PK는 필수 입력 값입니다.")
    private Long authorId;

    @NotBlank(message = "코스 제목은 필수 입력 값입니다.")
    private String courseTitle;

    @NotBlank(message = "코스 후기는 필수 입력 값입니다.")
    private String courseComment;

    @NotBlank(message = "코스 상태는 필수 입력 값입니다.")
    private CourseStatus courseStstus;
}
