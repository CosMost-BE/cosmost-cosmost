package com.cosmost.project.cosmost.service;

import com.cosmost.project.cosmost.infrastructure.entity.CourseEntity;
import com.cosmost.project.cosmost.infrastructure.repository.CourseEntityRepository;
import com.cosmost.project.cosmost.model.Course;
import com.cosmost.project.cosmost.requestbody.CreateCourseRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CosmostsServiceImpl implements CosmostsService {

    private final CourseEntityRepository courseEntityRepository;

    @Autowired
    public CosmostsServiceImpl(CourseEntityRepository courseEntityRepository) {
        this.courseEntityRepository = courseEntityRepository;
    }

    // 코스 등록
    @Override
    public CourseEntity createCourse(CreateCourseRequest createCourseRequest) {
        CourseEntity courseEntity = createCourseRequest.createDtoToEntity(createCourseRequest);
        courseEntityRepository.save(courseEntity);

        return courseEntity;
    }

}
