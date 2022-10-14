package com.cosmost.project.cosmost.responsebody;

import com.cosmost.project.cosmost.infrastructure.entity.CourseStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReadCourseResponse {

    private Long id;
    private Long authorId;

    private String courseTitle;
    private String courseComment;
    private CourseStatus courseStatus;

}
