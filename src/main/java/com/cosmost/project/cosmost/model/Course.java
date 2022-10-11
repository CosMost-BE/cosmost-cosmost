package com.cosmost.project.cosmost.model;

import com.cosmost.project.cosmost.infrastructure.entity.CourseStatus;
import lombok.*;


@Builder
@Getter
@ToString
public class Course {

    private Long id;
    private Long authorId;
    private String courseTitle;
    private String courseComment;
    private CourseStatus courseStstus;
}
