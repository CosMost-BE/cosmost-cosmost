package com.cosmost.project.cosmost.service;

import com.cosmost.project.cosmost.infrastructure.entity.CourseEntity;
import com.cosmost.project.cosmost.requestbody.CreateCourseRequest;
import com.cosmost.project.cosmost.requestbody.UpdateCourseRequest;
import com.cosmost.project.cosmost.view.CourseView;

import java.util.List;
import java.util.Optional;

public interface CosmostsService {
    CourseEntity createCourse(CreateCourseRequest createCourseRequest);
    Optional<CourseEntity> updateCourse(Long id, UpdateCourseRequest updateCourseRequest);
    Optional<CourseEntity> deleteCourse(Long id);
    List<CourseView> readCourseByAuthId();
    CourseView readCourseByCourseId(Long id);




}
