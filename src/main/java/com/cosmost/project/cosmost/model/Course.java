package com.cosmost.project.cosmost.model;

import com.cosmost.project.cosmost.infrastructure.entity.CourseEntity;
import com.cosmost.project.cosmost.infrastructure.entity.CourseStatus;
import com.cosmost.project.cosmost.infrastructure.entity.PlaceDetailEntity;
import lombok.*;

import java.time.LocalDate;
import java.util.List;


@Getter
@ToString
@Builder
@AllArgsConstructor
public class Course {

    private Long id;
    private Long authorId;
    private String courseTitle;
    private String courseComment;
    private CourseStatus courseStatus;
    private LocalDate createAt;


    private List<PlaceDetail> placeDetailList;
    private List<Hashtag> hashtagList;
    private List<PlaceImg> placeImgList;
    private List<CategoryList> categoryLists;


    public Course(CourseEntity courseEntity) {
        this.id = courseEntity.getId();
        this.authorId = courseEntity.getAuthorId();
        this.courseTitle = courseEntity.getCourseTitle();
        this.courseComment = courseEntity.getCourseComment();
        this.courseStatus = courseEntity.getCourseStatus();
        this.placeDetailList = getPlaceDetailList();
        this.hashtagList = getHashtagList();
        this.placeImgList = getPlaceImgList();
        this.categoryLists = getCategoryLists();
        this.createAt = getCreateAt();

    }
}
