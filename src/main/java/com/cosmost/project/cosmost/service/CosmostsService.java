package com.cosmost.project.cosmost.service;

import com.cosmost.project.cosmost.infrastructure.entity.CourseEntity;
import com.cosmost.project.cosmost.model.Course;
import com.cosmost.project.cosmost.requestbody.CreateCourseRequest;
import com.cosmost.project.cosmost.requestbody.UpdateCourseRequest;
import com.cosmost.project.cosmost.responsebody.ReadCourseResponse;
import com.cosmost.project.cosmost.view.CourseView;

import java.util.List;
import java.util.Optional;

public interface CosmostsService {
    CourseEntity createCourse(CreateCourseRequest createCourseRequest);
    Optional<CourseEntity> updateCourse(Long id, UpdateCourseRequest updateCourseRequest);
    void deleteCourse(Long id);
    List<Course> readCourseByAuthId();
    ReadCourseResponse readCourseByCourseId(Long id);




}
