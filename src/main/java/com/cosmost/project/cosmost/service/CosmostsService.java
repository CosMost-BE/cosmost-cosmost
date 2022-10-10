package com.cosmost.project.cosmost.service;

import com.cosmost.project.cosmost.infrastructure.entity.CourseEntity;
import com.cosmost.project.cosmost.model.Course;
import com.cosmost.project.cosmost.requestbody.CreateCourseRequest;

import java.util.List;

public interface CosmostsService {
    CourseEntity createCourse(CreateCourseRequest createCourseRequest);

}
