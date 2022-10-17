package com.cosmost.project.cosmost.service;

import com.cosmost.project.cosmost.model.Course;
import com.cosmost.project.cosmost.requestbody.CreateCourseRequest;
import com.cosmost.project.cosmost.requestbody.UpdateCourseRequest;
import com.cosmost.project.cosmost.responsebody.ReadCourseResponse;

import java.util.List;

public interface CosmostsService {
    void createCourse(CreateCourseRequest createCourseRequest);
    void updateCourse(Long id, UpdateCourseRequest updateCourseRequest);
    void deleteCourse(Long id);
    List<ReadCourseResponse> readCourseByAuthId();
    Course readCourseByCourseId(Long id);




}
