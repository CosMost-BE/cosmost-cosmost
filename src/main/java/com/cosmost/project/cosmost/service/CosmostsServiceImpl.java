package com.cosmost.project.cosmost.service;

import com.cosmost.project.cosmost.exception.CourseIdNotfound;
import com.cosmost.project.cosmost.infrastructure.entity.CourseEntity;
import com.cosmost.project.cosmost.infrastructure.entity.PlaceDetailEntity;
import com.cosmost.project.cosmost.infrastructure.repository.CourseEntityRepository;
import com.cosmost.project.cosmost.infrastructure.repository.HashtagEntityRepository;
import com.cosmost.project.cosmost.infrastructure.repository.PlaceDetailEntityRepository;
import com.cosmost.project.cosmost.model.Course;
import com.cosmost.project.cosmost.model.PlaceDetail;
import com.cosmost.project.cosmost.requestbody.CreateCourseRequest;
import com.cosmost.project.cosmost.requestbody.CreateHashtagRequest;
import com.cosmost.project.cosmost.requestbody.CreatePlaceDetailRequest;
import com.cosmost.project.cosmost.requestbody.UpdateCourseRequest;
import com.cosmost.project.cosmost.responsebody.ReadCourseResponse;
import com.cosmost.project.cosmost.responsebody.ReadPlaceDetailResponse;
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
import java.util.stream.Collectors;

@Slf4j
@Service
public class CosmostsServiceImpl implements CosmostsService {

    private final CourseEntityRepository courseEntityRepository;
    private final PlaceDetailEntityRepository placeDetailEntityRepository;
    private final HashtagEntityRepository hashtagEntityRepository;

    @Autowired
    public CosmostsServiceImpl(CourseEntityRepository courseEntityRepository, PlaceDetailEntityRepository placeDetailEntityRepository,
                               HashtagEntityRepository hashtagEntityRepository) {
        this.courseEntityRepository = courseEntityRepository;
        this.placeDetailEntityRepository = placeDetailEntityRepository;
        this.hashtagEntityRepository = hashtagEntityRepository;
    }

    // 코스 등록
    @Transactional
    @Override
    public void createCourse(CreateCourseRequest createCourseRequest) {
        CourseEntity courseEntity = createCourseRequest.createDtoToEntity(createCourseRequest);
        courseEntityRepository.save(courseEntity);

        for(CreatePlaceDetailRequest placeDetailRequest : createCourseRequest.getCreatePlaceDetailRequestList()) {
            placeDetailEntityRepository.save(placeDetailRequest.createDtoToEntity(placeDetailRequest, courseEntity));
        }

        for(CreateHashtagRequest hashtagRequest : createCourseRequest.getCreateHashtagRequestList()) {
            hashtagEntityRepository.save(hashtagRequest.createDtoToEntity(hashtagRequest, courseEntity));
        }
    }

    // 코스 수정
    @Transactional
    @Override
    public void updateCourse(Long id, UpdateCourseRequest updateCourseRequest) {

        Optional<CourseEntity> courseEntityCheck = Optional.ofNullable(
                courseEntityRepository.findById(id).orElseThrow(
                        CourseIdNotfound::new));

        if (courseEntityCheck.isPresent()) {
            CourseEntity courseEntity = updateCourseRequest.updateDtoToEntity(id, updateCourseRequest);
            courseEntityRepository.save(courseEntity);

            List<PlaceDetailEntity> placeDetailEntityList = placeDetailEntityRepository.findAllByCourse(courseEntityCheck.get());

            for (PlaceDetailEntity temp : placeDetailEntityList) {
                placeDetailEntityRepository.deleteById(temp.getId());
            }

            for (CreatePlaceDetailRequest placeDetailRequest : updateCourseRequest.getCreatePlaceDetailRequestList()) {
                placeDetailEntityRepository.save(placeDetailRequest.createDtoToEntity(placeDetailRequest, courseEntity));
            }
        }
    }

    // 코스 삭제
    @Transactional
    @Override
    public void deleteCourse(Long id) {

        Optional<CourseEntity> courseEntityCheck = Optional.ofNullable(
                courseEntityRepository.findById(id).orElseThrow(
                        CourseIdNotfound::new));

        if(courseEntityCheck.isPresent()) {
            List<PlaceDetailEntity> placeDetailEntityList = placeDetailEntityRepository.findAllByCourse(courseEntityCheck.get());

            for (PlaceDetailEntity temp : placeDetailEntityList) {
                placeDetailEntityRepository.deleteById(temp.getId());
            }

            courseEntityRepository.deleteById(id);
        }
    }

    // 작성한 코스 목록 조회
    @Override
    public List<ReadCourseResponse> readCourseByAuthId() {
        // header 내에 Authorization으로 있는 기본키를 반환
        HttpServletRequest request = ((ServletRequestAttributes)
                RequestContextHolder.currentRequestAttributes()).getRequest();
        Long authorId = Long.parseLong(request.getHeader("Authorization"));

        List<CourseEntity> courseEntityList = courseEntityRepository.findAllByAuthorId(authorId);
        List<ReadCourseResponse> courseList = new ArrayList<>();


        courseEntityList.forEach(courseEntity -> {
            List<PlaceDetailEntity> placeDetailEntityList = placeDetailEntityRepository.findByCourse_Id(courseEntity.getId());
            List<ReadPlaceDetailResponse> readPlaceDetailResponseList = new ArrayList<>();

            placeDetailEntityList.forEach(placeDetailEntity -> {
                readPlaceDetailResponseList.add(ReadPlaceDetailResponse.builder()
                        .id(placeDetailEntity.getId())
                        .placeName(placeDetailEntity.getPlaceName())
                        .placeOrder(placeDetailEntity.getPlaceOrder())
                        .build());
            });

            courseList.add(ReadCourseResponse.builder()
                            .id(courseEntity.getId())
                            .authorId(courseEntity.getAuthorId())
                            .courseTitle(courseEntity.getCourseTitle())
                            .courseStatus(courseEntity.getCourseStatus())
                            .readPlaceDetailResponseList(readPlaceDetailResponseList)
                    .build());
        });

        return courseList;

    }

    // 코스 한 개 상세 조회
    @Override
    public Course readCourseByCourseId(Long id) {

        Optional<CourseEntity> courseEntityCheck = Optional.ofNullable(
                courseEntityRepository.findById(id).orElseThrow(
                        CourseIdNotfound::new));

        if(courseEntityCheck.isPresent()) {

            List<PlaceDetailEntity> placeDetailEntityList = placeDetailEntityRepository.findAllByCourse(courseEntityCheck.get());
            List<PlaceDetail> placeDetailList = new ArrayList<>();

            placeDetailEntityList.forEach(placeDetailEntity -> {
                placeDetailList.add(PlaceDetail.builder()
                        .id(placeDetailEntity.getId())
                        .placeName(placeDetailEntity.getPlaceName())
                        .placeComment(placeDetailEntity.getPlaceComment())
                        .placeOrder(placeDetailEntity.getPlaceOrder())
                        .placeUrl(placeDetailEntity.getPlaceUrl())
                        .build());
            });

            return Course.builder()
                    .id(courseEntityCheck.get().getId())
                    .authorId(courseEntityCheck.get().getAuthorId())
                    .courseTitle(courseEntityCheck.get().getCourseTitle())
                    .courseComment(courseEntityCheck.get().getCourseComment())
                    .courseStatus(courseEntityCheck.get().getCourseStatus())
                    .placeDetailList(placeDetailList)
                    .build();
        }

        return null;
    }

}
