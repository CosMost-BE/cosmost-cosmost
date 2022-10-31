package com.cosmost.project.cosmost.service;

import com.cosmost.project.cosmost.infrastructure.entity.CourseEntity;
import com.cosmost.project.cosmost.model.Course;
import com.cosmost.project.cosmost.requestbody.CreateCourseRequest;
import com.cosmost.project.cosmost.requestbody.UpdateCourseRequest;
import com.cosmost.project.cosmost.responsebody.ReadCourseResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CosmostsService {
    void createCourse(CreateCourseRequest createCourseRequest, List<MultipartFile> file);
    void updateCourse(Long id, UpdateCourseRequest updateCourseRequest, List<MultipartFile> file);
    void deleteCourse(Long id);
    List<ReadCourseResponse> readCourseByAuthId(Pageable pageable);
    Course readCourseByCourseId(Long id);
    List<ReadCourseResponse> readCourseAll(Pageable pageable);





}
