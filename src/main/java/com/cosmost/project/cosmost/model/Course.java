package com.cosmost.project.cosmost.model;

import com.cosmost.project.cosmost.infrastructure.entity.CourseEntity;
import com.cosmost.project.cosmost.infrastructure.entity.CourseStatus;
import lombok.*;


@Getter
@ToString
public class Course {

    private Long id;
    private Long authorId;
    private String courseTitle;
    private String courseComment;
    private CourseStatus courseStatus;

    public Course(CourseEntity courseEntity) {
        this.id = courseEntity.getId();
        this.authorId = courseEntity.getAuthorId();
        this.courseTitle = courseEntity.getCourseTitle();
        this.courseComment = courseEntity.getCourseComment();
        courseStatus = courseEntity.getCourseStatus();
    }
}
