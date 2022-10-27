package com.cosmost.project.cosmost.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.cosmost.project.cosmost.exception.CourseIdNotfound;
import com.cosmost.project.cosmost.infrastructure.entity.*;
import com.cosmost.project.cosmost.infrastructure.repository.*;
import com.cosmost.project.cosmost.model.*;
import com.cosmost.project.cosmost.requestbody.*;
import com.cosmost.project.cosmost.responsebody.ReadCourseResponse;
import com.cosmost.project.cosmost.responsebody.ReadPlaceDetailResponse;
import com.cosmost.project.cosmost.responsebody.ReadPlaceImgResponse;
import com.cosmost.project.cosmost.util.AmazonS3ResourceStorage;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CosmostsServiceImpl implements CosmostsService {

    private final CourseEntityRepository courseEntityRepository;
    private final PlaceDetailEntityRepository placeDetailEntityRepository;
    private final HashtagEntityRepository hashtagEntityRepository;
    private final PlaceImgEntityRepository placeImgEntityRepository;
    private final AmazonS3ResourceStorage amazonS3ResourceStorage;
    private final CategoryListRepository categoryListRepository;
    private final LocationCategoryRepository locationCategoryRepository;
    private final ThemeCategoryRepository themeCategoryRepository;


    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${jwt.secret}")
    private String secretKey;

    @Autowired
    public CosmostsServiceImpl(CourseEntityRepository courseEntityRepository, PlaceDetailEntityRepository placeDetailEntityRepository,
                               HashtagEntityRepository hashtagEntityRepository, PlaceImgEntityRepository placeImgEntityRepository,
                               AmazonS3ResourceStorage amazonS3ResourceStorage, AmazonS3 amazonS3,
                               CategoryListRepository categoryListRepository, LocationCategoryRepository locationCategoryRepository,
                               ThemeCategoryRepository themeCategoryRepository) {
        this.courseEntityRepository = courseEntityRepository;
        this.placeDetailEntityRepository = placeDetailEntityRepository;
        this.hashtagEntityRepository = hashtagEntityRepository;
        this.placeImgEntityRepository = placeImgEntityRepository;
        this.amazonS3ResourceStorage = amazonS3ResourceStorage;
        this.amazonS3 = amazonS3;
        this.categoryListRepository = categoryListRepository;
        this.locationCategoryRepository = locationCategoryRepository;
        this.themeCategoryRepository = themeCategoryRepository;

    }

    // 코스 등록
    @Transactional
    @Override
    public void createCourse(CreateCourseRequest createCourseRequest, List<MultipartFile> file) {
        // header 내에 Authorization으로 있는 기본키를 반환
        HttpServletRequest request = ((ServletRequestAttributes)
                RequestContextHolder.currentRequestAttributes()).getRequest();

        String token = request.getHeader("Authorization");
        Long authorId = Long.parseLong(Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject());
        int count = 0;

        CourseEntity courseEntity = createCourseRequest.createDtoToEntity(createCourseRequest, authorId);
        courseEntityRepository.save(courseEntity);

        for(CreatePlaceDetailRequest placeDetailRequest : createCourseRequest.getCreatePlaceDetailRequestList()) {
            placeDetailEntityRepository.save(placeDetailRequest.createDtoToEntity(placeDetailRequest, courseEntity));
        }

        for(CreateHashtagRequest hashtagRequest : createCourseRequest.getCreateHashtagRequestList()) {
            hashtagEntityRepository.save(hashtagRequest.createDtoToEntity(hashtagRequest, courseEntity));
        }

        List<FileInfoRequest> returnDto = new ArrayList<>();

        for(CreatePlaceImgRequest placeImgRequest : createCourseRequest.getCreatePlaceImgRequestList()) {
            FileInfoRequest fileInfoRequest = FileInfoRequest.multipartOf(file.get(count), "place_img"); // 폴더이름
            amazonS3ResourceStorage.store(fileInfoRequest, file.get(count));
            returnDto.add(fileInfoRequest);

            placeImgEntityRepository.save(placeImgRequest.createDtoToEntity(courseEntity, fileInfoRequest, placeImgRequest));
            count += 1;
        }


        for(CreateCategoryListRequest categoryListRequest : createCourseRequest.getCreateCategoryListRequestList()) {
            Optional<LocationCategoryEntity> locationCategory = locationCategoryRepository.findById(categoryListRequest.getLocationCategory());
            Optional<ThemeCategoryEntity> themeCategory = themeCategoryRepository.findById(categoryListRequest.getThemeCategory());

            categoryListRepository.save(categoryListRequest.createDtoToEntity(courseEntity, locationCategory.get(), themeCategory.get()));
        }


    }

    // 코스 수정
    @Transactional
    @Override
    public void updateCourse(Long id, UpdateCourseRequest updateCourseRequest, List<MultipartFile> file) {
        HttpServletRequest request = ((ServletRequestAttributes)
                RequestContextHolder.currentRequestAttributes()).getRequest();

        String token = request.getHeader("Authorization");
        Long authorId = Long.parseLong(Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject());
        int count = 0;

        Optional<CourseEntity> courseEntityCheck = Optional.ofNullable(
                courseEntityRepository.findById(id).orElseThrow(
                        CourseIdNotfound::new));

        if (courseEntityCheck.isPresent()) {
            CourseEntity courseEntity = updateCourseRequest.updateDtoToEntity(id, updateCourseRequest, authorId);
            courseEntityRepository.save(courseEntity);

            List<PlaceDetailEntity> placeDetailEntityList = placeDetailEntityRepository.findAllByCourse(courseEntityCheck.get());
            List<HashtagEntity> hashtagEntityList = hashtagEntityRepository.findAllByCourse(courseEntityCheck.get());

            for (PlaceDetailEntity temp : placeDetailEntityList) {
                placeDetailEntityRepository.deleteById(temp.getId());
            }

            for (HashtagEntity temp : hashtagEntityList) {
                hashtagEntityRepository.deleteById(temp.getId());
            }


            if(!updateCourseRequest.getDeletePlaceImgRequestList().isEmpty()) {

                for (int i=0; i<updateCourseRequest.getDeletePlaceImgRequestList().size(); i++) {
                    placeImgEntityRepository.deleteById(updateCourseRequest.getDeletePlaceImgRequestList().get(i));
                }
            }

            if(!updateCourseRequest.getUpdatePlaceImgRequestList().isEmpty()) {
                for (UpdatePlaceImgRequest placeImgRequest : updateCourseRequest.getUpdatePlaceImgRequestList()) {
                    placeImgEntityRepository.save(placeImgRequest.updateDtoToEntity(placeImgRequest, courseEntity));
                }
            }


            for (CreatePlaceDetailRequest placeDetailRequest : updateCourseRequest.getCreatePlaceDetailRequestList()) {
                placeDetailEntityRepository.save(placeDetailRequest.createDtoToEntity(placeDetailRequest, courseEntity));
            }

            for (CreateHashtagRequest hashtagRequest : updateCourseRequest.getCreateHashtagRequestList()) {
                hashtagEntityRepository.save(hashtagRequest.createDtoToEntity(hashtagRequest, courseEntity));
            }

            if (!file.get(0).isEmpty()) {
                List<FileInfoRequest> returnDto = new ArrayList<>();

                for(CreatePlaceImgRequest placeImgRequest : updateCourseRequest.getCreatePlaceImgRequestList()) {
                    FileInfoRequest fileInfoRequest = FileInfoRequest.multipartOf(file.get(count), "place_img"); // 폴더이름
                    amazonS3ResourceStorage.store(fileInfoRequest, file.get(count));
                    returnDto.add(fileInfoRequest);

                    placeImgEntityRepository.save(placeImgRequest.createDtoToEntity(courseEntity, fileInfoRequest, placeImgRequest));
                    count += 1;
                }

            }

            for(UpdateCategoryListRequest categoryListRequest : updateCourseRequest.getUpdateCategoryListRequestList()) {
                Optional<LocationCategoryEntity> locationCategory = locationCategoryRepository.findById(categoryListRequest.getLocationCategory());
                Optional<ThemeCategoryEntity> themeCategory = themeCategoryRepository.findById(categoryListRequest.getThemeCategory());

                categoryListRepository.save(categoryListRequest.updateDtoToEntity(courseEntity, locationCategory.get(), themeCategory.get(), categoryListRequest));
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
            List<HashtagEntity> hashtagEntityList = hashtagEntityRepository.findAllByCourse(courseEntityCheck.get());
            List<PlaceImgEntity> placeImgEntityList = placeImgEntityRepository.findAllByCourse(courseEntityCheck.get());
            List<CategoryListEntity> categoryListEntity = categoryListRepository.findByCourse_Id(courseEntityCheck.get().getId());

            for (PlaceDetailEntity temp : placeDetailEntityList) {
                placeDetailEntityRepository.deleteById(temp.getId());
            }

            for (HashtagEntity temp : hashtagEntityList) {
                hashtagEntityRepository.deleteById(temp.getId());
            }

            for (PlaceImgEntity temp : placeImgEntityList) {
                placeImgEntityRepository.deleteById(temp.getId());
            }

            for (CategoryListEntity temp : categoryListEntity) {
                categoryListRepository.deleteById(temp.getId());
            }

            courseEntityRepository.deleteById(id);
        }
    }

    // 작성한 코스 목록 조회
    @Override
    public List<ReadCourseResponse> readCourseByAuthId() {
        HttpServletRequest request = ((ServletRequestAttributes)
                RequestContextHolder.currentRequestAttributes()).getRequest();

        String token = request.getHeader("Authorization");
        Long authorId = Long.parseLong(Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject());


        List<CourseEntity> courseEntityList = courseEntityRepository.findAllByAuthorId(authorId);
        List<ReadCourseResponse> courseList = new ArrayList<>();


        courseEntityList.forEach(courseEntity -> {


            List<PlaceDetailEntity> placeDetailEntityList = placeDetailEntityRepository.findByCourse_Id(courseEntity.getId());
            List<ReadPlaceDetailResponse> readPlaceDetailResponseList = new ArrayList<>();

            List<HashtagEntity> hashtagEntityList = hashtagEntityRepository.findByCourse_Id(courseEntity.getId());
            List<Hashtag> hashtagList = new ArrayList<>();

            List<PlaceImgEntity> placeImgEntityList = placeImgEntityRepository.findByCourse_IdAndAndPlaceImgOrder(courseEntity.getId(), 0);
            List<ReadPlaceImgResponse> readPlaceImgResponseList = new ArrayList<>();

            List<CategoryListEntity> categoryListEntityList = categoryListRepository.findByCourse_Id(courseEntity.getId());
            List<CategoryList> categoryLists = new ArrayList<>();


            placeDetailEntityList.forEach(placeDetailEntity -> {
                readPlaceDetailResponseList.add(ReadPlaceDetailResponse.builder()
                        .id(placeDetailEntity.getId())
                        .placeName(placeDetailEntity.getPlaceName())
                        .placeOrder(placeDetailEntity.getPlaceOrder())
                        .build());
            });

            hashtagEntityList.forEach(hashtagEntity -> {
                hashtagList.add(Hashtag.builder()
                        .id(hashtagEntity.getId())
                        .keyword(hashtagEntity.getKeyword())
                        .build());
            });

            placeImgEntityList.forEach(placeImgEntity -> {
                readPlaceImgResponseList.add(ReadPlaceImgResponse.builder()
                        .id(placeImgEntity.getId())
                        .placeImgOrder(placeImgEntity.getPlaceImgOrder())
                        .placeImgUrl(placeImgEntity.getPlaceImgUrl())
                        .build());
            });

            categoryListEntityList.forEach(categoryListEntity -> {
                categoryLists.add(CategoryList.builder()
                        .id(categoryListEntity.getId())
                        .locationCategoryName(categoryListEntity.getLocationCategory().getLocationCategoryName())
                        .themeCategoryName(categoryListEntity.getThemeCategory().getThemeCategoryName())
                        .build());
            });

            courseList.add(ReadCourseResponse.builder()
                    .id(courseEntity.getId())
                    .authorId(courseEntity.getAuthorId())
                    .courseTitle(courseEntity.getCourseTitle())
                    .courseStatus(courseEntity.getCourseStatus())
                    .createAt(courseEntity.getCreateAt())
                    .readPlaceDetailResponseList(readPlaceDetailResponseList)
                    .hashtagList(hashtagList)
                    .readPlaceImgResponseList(readPlaceImgResponseList)
                    .categoryLists(categoryLists)
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

            List<HashtagEntity> hashtagEntityList = hashtagEntityRepository.findAllByCourse(courseEntityCheck.get());
            List<Hashtag> hashtagList = new ArrayList<>();

            List<PlaceImgEntity> placeImgEntityList = placeImgEntityRepository.findAllByCourse(courseEntityCheck.get());
            List<PlaceImg> placeImgList = new ArrayList<>();

            List<CategoryListEntity> categoryListEntityList = categoryListRepository.findByCourse_Id(courseEntityCheck.get().getId());
            List<CategoryList> categoryLists = new ArrayList<>();

            placeDetailEntityList.forEach(placeDetailEntity -> {
                placeDetailList.add(PlaceDetail.builder()
                        .id(placeDetailEntity.getId())
                        .placeName(placeDetailEntity.getPlaceName())
                        .placeComment(placeDetailEntity.getPlaceComment())
                        .placeOrder(placeDetailEntity.getPlaceOrder())
                        .placeXCoordinate(placeDetailEntity.getPlaceXCoordinate())
                        .placeYCoordinate(placeDetailEntity.getPlaceYCoordinate())
                        .build());
            });

            hashtagEntityList.forEach(hashtagEntity -> {
                hashtagList.add(Hashtag.builder()
                        .id(hashtagEntity.getId())
                        .keyword(hashtagEntity.getKeyword())
                        .build());
            });

            placeImgEntityList.forEach(placeImgEntity -> {
                placeImgList.add(PlaceImg.builder()
                        .id(placeImgEntity.getId())
                        .placeImgOriginName(placeImgEntity.getPlaceImgOriginName())
                        .placeImgSaveName(placeImgEntity.getPlaceImgSaveName())
                        .placeImgUrl(placeImgEntity.getPlaceImgUrl())
                        .placeImgOrder(placeImgEntity.getPlaceImgOrder())
                        .build());
            });

            categoryListEntityList.forEach(categoryListEntity -> {
                categoryLists.add(CategoryList.builder()
                        .id(categoryListEntity.getId())
                        .locationCategoryName(categoryListEntity.getLocationCategory().getLocationCategoryName())
                        .themeCategoryName(categoryListEntity.getThemeCategory().getThemeCategoryName())
                        .build());
            });

            return Course.builder()
                    .id(courseEntityCheck.get().getId())
                    .authorId(courseEntityCheck.get().getAuthorId())
                    .courseTitle(courseEntityCheck.get().getCourseTitle())
                    .courseComment(courseEntityCheck.get().getCourseComment())
                    .courseStatus(courseEntityCheck.get().getCourseStatus())
                    .createAt(courseEntityCheck.get().getCreateAt())
                    .placeDetailList(placeDetailList)
                    .hashtagList(hashtagList)
                    .placeImgList(placeImgList)
                    .categoryLists(categoryLists)
                    .build();
        }

        return null;
    }

}
