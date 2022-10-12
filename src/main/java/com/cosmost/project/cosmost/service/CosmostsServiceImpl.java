package com.cosmost.project.cosmost.service;

import com.cosmost.project.cosmost.exception.CourseIdNotfound;
import com.cosmost.project.cosmost.infrastructure.entity.CourseEntity;
import com.cosmost.project.cosmost.infrastructure.repository.CourseEntityRepository;
import com.cosmost.project.cosmost.model.Course;
import com.cosmost.project.cosmost.requestbody.CreateCourseRequest;
import com.cosmost.project.cosmost.requestbody.UpdateCourseRequest;
import com.cosmost.project.cosmost.view.CourseView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CosmostsServiceImpl implements CosmostsService {

    private final CourseEntityRepository courseEntityRepository;

    @Autowired
    public CosmostsServiceImpl(CourseEntityRepository courseEntityRepository) {
        this.courseEntityRepository = courseEntityRepository;
    }

    // 코스 등록
    @Transactional
    @Override
    public CourseEntity createCourse(CreateCourseRequest createCourseRequest) {
        CourseEntity courseEntity = createCourseRequest.createDtoToEntity(createCourseRequest);
        courseEntityRepository.save(courseEntity);

        return courseEntity;
    }

    // 코스 수정
    @Transactional
    @Override
    public Optional<CourseEntity> updateCourse(Long id, UpdateCourseRequest updateCourseRequest) {

        Optional<CourseEntity> courseEntityCheck = Optional.ofNullable(
                courseEntityRepository.findById(id).orElseThrow(
                        CourseIdNotfound::new));

        if(courseEntityCheck.isPresent()) {
            CourseEntity courseEntity = updateCourseRequest.updateDtoToEntity(id, updateCourseRequest);
            courseEntityRepository.save(courseEntity);

            return courseEntityCheck;
        }

        return null;
    }

    // 코스 삭제
    @Transactional
    @Override
    public Optional<CourseEntity> deleteCourse(Long id) {

        Optional<CourseEntity> courseEntityCheck = Optional.ofNullable(
                courseEntityRepository.findById(id).orElseThrow(
                        CourseIdNotfound::new));

        if(courseEntityCheck.isPresent()) {
            courseEntityRepository.deleteById(id);
            return courseEntityCheck;
        }

        return null;
    }

    @Override
    public List<CourseView> readCourseByAuthId(Long authorId) {

        List<Course> courseList = courseEntityRepository.findAllByAuthorId(authorId);
        List<CourseView> courseViewList = new ArrayList<>();

        courseList.forEach(course -> {
            courseViewList.add(CourseView.builder()
                    .id(course.getId())
                    .authorId(course.getAuthorId())
                    .courseTitle(course.getCourseTitle())
                    .courseComment(course.getCourseComment())
                    .courseStatus(course.getCourseStatus())
                    .build());
        });

        return courseViewList;
    }





}
