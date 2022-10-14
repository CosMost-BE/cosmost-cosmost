package com.cosmost.project.cosmost.view;

import com.cosmost.project.cosmost.infrastructure.entity.CourseStatus;
import com.cosmost.project.cosmost.model.Course;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString // toString() 메소드를 생성
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CourseView {
    private Long id;

    private Long authorId;
    private String courseTitle;
    private String courseComment;
    private CourseStatus courseStatus;

    public CourseView(Course course) {
        this.id = course.getId();
        this.authorId = course.getAuthorId();
        this.courseTitle = course.getCourseTitle();
        this.courseComment = course.getCourseComment();
        this.courseStatus = course.getCourseStatus();
    }
}
