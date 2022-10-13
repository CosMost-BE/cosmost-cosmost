package com.cosmost.project.cosmost.service;

import com.cosmost.project.cosmost.exception.CourseIdNotfound;
import com.cosmost.project.cosmost.infrastructure.entity.CourseEntity;
import com.cosmost.project.cosmost.infrastructure.repository.CourseEntityRepository;
import com.cosmost.project.cosmost.requestbody.CreateCourseRequest;
import com.cosmost.project.cosmost.requestbody.UpdateCourseRequest;
import com.cosmost.project.cosmost.view.CourseView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
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

    // 작성한 코스 목록 조회
    @Override
    public List<CourseView> readCourseByAuthId() {
        List<CourseEntity> courseList = courseEntityRepository.findAllByAuthorId(getIdByHeader());
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

    // 코스 한 개 상세 조회
    @Override
    public CourseView readCourseByCourseId(Long id) {

        CourseEntity courseEntity = courseEntityRepository.findById(id)
                .orElseThrow(CourseIdNotfound::new);

        if(!courseEntity.equals(null)) {

            return CourseView.builder()
                    .id(courseEntity.getId())
                    .authorId(courseEntity.getAuthorId())
                    .courseTitle(courseEntity.getCourseTitle())
                    .courseComment(courseEntity.getCourseComment())
                    .courseStatus(courseEntity.getCourseStatus())
                    .build();
        }

        return null;
    }


    // header 내에 Authorization으로 있는 기본키를 반환
    private Long getIdByHeader() {
        HttpServletRequest request = ((ServletRequestAttributes)
                RequestContextHolder.currentRequestAttributes()).getRequest();
        Long id = Long.parseLong(request.getHeader("Authorization"));

        return id;
    }

}
