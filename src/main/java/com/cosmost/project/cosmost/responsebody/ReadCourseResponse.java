package com.cosmost.project.cosmost.responsebody;

import com.cosmost.project.cosmost.infrastructure.entity.CourseStatus;
import com.cosmost.project.cosmost.model.CategoryList;
import com.cosmost.project.cosmost.model.Hashtag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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
    private LocalDate createAt;
    private boolean whetherLastPage;

    private List<ReadPlaceDetailResponse> readPlaceDetailResponseList;
    private List<Hashtag> hashtagList;
    private List<ReadPlaceImgResponse> readPlaceImgResponseList;
    private List<CategoryList> categoryLists;

}
