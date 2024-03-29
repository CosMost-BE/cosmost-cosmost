package com.cosmost.project.cosmost.service;

import com.amazonaws.services.s3.AmazonS3;
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
import org.springframework.data.domain.*;
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

            if (file != null && !file.isEmpty() && !file.get(0).isEmpty()) {
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
        HttpServletRequest request = ((ServletRequestAttributes)
                RequestContextHolder.currentRequestAttributes()).getRequest();

        String token = request.getHeader("Authorization");
        Long authorId = Long.parseLong(Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject());


        Optional<CourseEntity> courseEntityCheck = Optional.ofNullable(
                courseEntityRepository.findAllByIdAndAuthorId(id, authorId).orElseThrow(
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
    public List<ReadCourseResponse> readCourseByAuthId(Pageable pageable) {
        HttpServletRequest request = ((ServletRequestAttributes)
                RequestContextHolder.currentRequestAttributes()).getRequest();

        String token = request.getHeader("Authorization");
        Long authorId = Long.parseLong(Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject());


        Slice<CourseEntity> courseEntityList = courseEntityRepository.findAllByAuthorId(authorId, pageable);
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
                    .whetherLastPage(courseEntityList.isLast())
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


            hashtagEntityList.forEach(hashtagEntity -> {
                hashtagList.add(Hashtag.builder()
                        .id(hashtagEntity.getId())
                        .keyword(hashtagEntity.getKeyword())
                        .build());
            });

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
                    .authorCourseCount(courseEntityRepository.countByAuthorId(courseEntityCheck.get().getAuthorId()))
                    .placeDetailList(placeDetailList)
                    .hashtagList(hashtagList)
                    .placeImgList(placeImgList)
                    .categoryLists(categoryLists)
                    .build();
        }

        return null;
    }

    // 코스 전체 목록 조회
    @Override
    public List<ReadCourseResponse> readCourseAll(Pageable pageable) {

        Slice<CourseEntity> courseEntityList = courseEntityRepository.findAll(pageable);
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
                    .whetherLastPage(courseEntityList.isLast())
                    .readPlaceDetailResponseList(readPlaceDetailResponseList)
                    .hashtagList(hashtagList)
                    .readPlaceImgResponseList(readPlaceImgResponseList)
                    .categoryLists(categoryLists)
                    .build());
        });

        return courseList;
    }

    // 코스 검색 목록 조회, 카테고리로 코스 검색 목록 조회
    @Override
    public List<ReadCourseResponse> readCourseByKeyword(String keyword, String category, Long nameId, Pageable pageable) {
        Slice<Long> searchCourseIdSlice = hashtagEntityRepository.searchCourseId(keyword, pageable);
        List<ReadCourseResponse> courseList = new ArrayList<>();
        List<CategoryListEntity> categoryLastPage = new ArrayList<>();

        boolean checkLastPage = searchCourseIdSlice.isLast();

        for(int i=0; i<searchCourseIdSlice.getContent().size(); i++) {

            if (category.equals("location")) {
                List<Long> searchCourseIdAndCategoryLastPage = hashtagEntityRepository.searchCourseIdAndCategoryLastPage(keyword, pageable);

                for(int j=0; j<searchCourseIdSlice.getContent().size(); j++) {
                    categoryLastPage = categoryListRepository.findByCourse_Id(searchCourseIdSlice.getContent().get(i).longValue());

                    if(categoryLastPage.equals(searchCourseIdAndCategoryLastPage)) {
                        break;
                    }
                }

                List<CategoryListEntity> categoryListEntitySlice = categoryListRepository.findByCourse_Id(searchCourseIdSlice.getContent().get(i).longValue());

                if(!categoryListEntitySlice.get(0).getLocationCategory().getId().equals(nameId)) {
                    continue;
                } else if (categoryListEntitySlice.equals(categoryLastPage)) {
                    checkLastPage = true;
                }
            } else if (category.equals("theme")) {
                List<CategoryListEntity> categoryListEntitySlice = categoryListRepository.findByCourse_Id(searchCourseIdSlice.getContent().get(i).longValue());

                if(!categoryListEntitySlice.get(0).getThemeCategory().getId().equals(nameId)) {
                    continue;
                }
            }

            List<PlaceDetailEntity> placeDetailEntityList = placeDetailEntityRepository.findByCourse_Id(searchCourseIdSlice.getContent().get(i).longValue());
            List<ReadPlaceDetailResponse> readPlaceDetailResponseList = new ArrayList<>();

            List<HashtagEntity> hashtagEntityList = hashtagEntityRepository.findByCourse_Id(searchCourseIdSlice.getContent().get(i).longValue());
            List<Hashtag> hashtagList = new ArrayList<>();

            List<PlaceImgEntity> placeImgEntityList = placeImgEntityRepository.findByCourse_IdAndAndPlaceImgOrder(searchCourseIdSlice.getContent().get(i).longValue(), 0);
            List<ReadPlaceImgResponse> readPlaceImgResponseList = new ArrayList<>();

            List<CategoryListEntity> categoryListEntityList = categoryListRepository.findByCourse_Id(searchCourseIdSlice.getContent().get(i).longValue());
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
                    .id(searchCourseIdSlice.getContent().get(i).longValue())
                    .authorId(courseEntityRepository.findById(searchCourseIdSlice.getContent().get(i).longValue()).get().getAuthorId())
                    .courseTitle(courseEntityRepository.findById(searchCourseIdSlice.getContent().get(i).longValue()).get().getCourseTitle())
                    .courseStatus(courseEntityRepository.findById(searchCourseIdSlice.getContent().get(i).longValue()).get().getCourseStatus())
                    .createAt(courseEntityRepository.findById(searchCourseIdSlice.getContent().get(i).longValue()).get().getCreateAt())
                    .whetherLastPage(checkLastPage)
                    .readPlaceDetailResponseList(readPlaceDetailResponseList)
                    .hashtagList(hashtagList)
                    .readPlaceImgResponseList(readPlaceImgResponseList)
                    .categoryLists(categoryLists)
                    .build());

        }

        return courseList;
    }


    // 코스 한 개 목록 조회
    @Override
    public ReadCourseResponse readCourseFrameByCourseId(Long id) {

        Optional<CourseEntity> courseEntityCheck = Optional.ofNullable(
                courseEntityRepository.findById(id).orElseThrow(
                        CourseIdNotfound::new));

        if(courseEntityCheck.isPresent()) {

            List<PlaceDetailEntity> placeDetailEntityList = placeDetailEntityRepository.findAllByCourse(courseEntityCheck.get());
            List<ReadPlaceDetailResponse> readPlaceDetailResponseList = new ArrayList<>();

            List<HashtagEntity> hashtagEntityList = hashtagEntityRepository.findAllByCourse(courseEntityCheck.get());
            List<Hashtag> hashtagList = new ArrayList<>();

            List<PlaceImgEntity> placeImgEntityList =  placeImgEntityRepository.findByCourse_IdAndAndPlaceImgOrder(courseEntityCheck.get().getId(), 0);
            List<ReadPlaceImgResponse> readPlaceImgResponseList = new ArrayList<>();

            List<CategoryListEntity> categoryListEntityList = categoryListRepository.findByCourse_Id(courseEntityCheck.get().getId());
            List<CategoryList> categoryLists = new ArrayList<>();


            hashtagEntityList.forEach(hashtagEntity -> {
                hashtagList.add(Hashtag.builder()
                        .id(hashtagEntity.getId())
                        .keyword(hashtagEntity.getKeyword())
                        .build());
            });

            placeDetailEntityList.forEach(placeDetailEntity -> {
                readPlaceDetailResponseList.add(ReadPlaceDetailResponse.builder()
                        .id(placeDetailEntity.getId())
                        .placeName(placeDetailEntity.getPlaceName())
                        .placeOrder(placeDetailEntity.getPlaceOrder())
                        .build());
            });



            placeImgEntityList.forEach(placeImgEntity -> {
                readPlaceImgResponseList.add(ReadPlaceImgResponse.builder()
                        .id(placeImgEntity.getId())
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

            return ReadCourseResponse.builder()
                    .id(courseEntityCheck.get().getId())
                    .authorId(courseEntityCheck.get().getAuthorId())
                    .courseTitle(courseEntityCheck.get().getCourseTitle())
                    .courseStatus(courseEntityCheck.get().getCourseStatus())
                    .createAt(courseEntityCheck.get().getCreateAt())
                    .readPlaceDetailResponseList(readPlaceDetailResponseList)
                    .hashtagList(hashtagList)
                    .readPlaceImgResponseList(readPlaceImgResponseList)
                    .categoryLists(categoryLists)
                    .build();
        }

        return null;
    }

    // 카테고리로 코스 전체 목록 조회
    @Override
    public List<ReadCourseResponse> readCourseByCategoryAll(String category, Long nameId, Pageable pageable) {

        Slice<CategoryListEntity> categoryListEntitySlice = null;

        if (category.equals("location")) {
            categoryListEntitySlice = categoryListRepository.findAllByLocationCategory_Id(nameId, pageable);
        } else if (category.equals("theme")) {
            categoryListEntitySlice = categoryListRepository.findAllByThemeCategory_Id(nameId, pageable);
        }

        List<ReadCourseResponse> courseList = new ArrayList<>();

        Slice<CategoryListEntity> finalCategoryListEntitySlice = categoryListEntitySlice;
        categoryListEntitySlice.forEach(courseEntity -> {


            List<PlaceDetailEntity> placeDetailEntityList = placeDetailEntityRepository.findByCourse_Id(courseEntity.getCourse().getId());
            List<ReadPlaceDetailResponse> readPlaceDetailResponseList = new ArrayList<>();

            List<HashtagEntity> hashtagEntityList = hashtagEntityRepository.findByCourse_Id(courseEntity.getCourse().getId());
            List<Hashtag> hashtagList = new ArrayList<>();

            List<PlaceImgEntity> placeImgEntityList = placeImgEntityRepository.findByCourse_IdAndAndPlaceImgOrder(courseEntity.getCourse().getId(), 0);
            List<ReadPlaceImgResponse> readPlaceImgResponseList = new ArrayList<>();

            List<CategoryListEntity> categoryListEntityList = categoryListRepository.findByCourse_Id(courseEntity.getCourse().getId());
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
                    .id(courseEntity.getCourse().getId())
                    .authorId(courseEntity.getCourse().getAuthorId())
                    .courseTitle(courseEntity.getCourse().getCourseTitle())
                    .courseStatus(courseEntity.getCourse().getCourseStatus())
                    .createAt(courseEntity.getCourse().getCreateAt())
                    .whetherLastPage(finalCategoryListEntitySlice.isLast())
                    .readPlaceDetailResponseList(readPlaceDetailResponseList)
                    .hashtagList(hashtagList)
                    .readPlaceImgResponseList(readPlaceImgResponseList)
                    .categoryLists(categoryLists)
                    .build());
        });

        return courseList;
    }

    // 해시태그별로 코스 조회
    @Override
    public List<ReadCourseResponse> readCourseByHashtag(String hashtag, Pageable pageable) {
        Slice<HashtagEntity> hashtagEntitySlice = hashtagEntityRepository.findAllByKeyword(hashtag, pageable);

        List<ReadCourseResponse> courseList = new ArrayList<>();

        hashtagEntitySlice.forEach(courseEntity -> {


            List<PlaceDetailEntity> placeDetailEntityList = placeDetailEntityRepository.findByCourse_Id(courseEntity.getCourse().getId());
            List<ReadPlaceDetailResponse> readPlaceDetailResponseList = new ArrayList<>();

            List<HashtagEntity> hashtagEntityList = hashtagEntityRepository.findByCourse_Id(courseEntity.getCourse().getId());
            List<Hashtag> hashtagList = new ArrayList<>();

            List<PlaceImgEntity> placeImgEntityList = placeImgEntityRepository.findByCourse_IdAndAndPlaceImgOrder(courseEntity.getCourse().getId(), 0);
            List<ReadPlaceImgResponse> readPlaceImgResponseList = new ArrayList<>();

            List<CategoryListEntity> categoryListEntityList = categoryListRepository.findByCourse_Id(courseEntity.getCourse().getId());
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
                    .id(courseEntity.getCourse().getId())
                    .authorId(courseEntity.getCourse().getAuthorId())
                    .courseTitle(courseEntity.getCourse().getCourseTitle())
                    .courseStatus(courseEntity.getCourse().getCourseStatus())
                    .createAt(courseEntity.getCourse().getCreateAt())
                    .whetherLastPage(hashtagEntitySlice.isLast())
                    .readPlaceDetailResponseList(readPlaceDetailResponseList)
                    .hashtagList(hashtagList)
                    .readPlaceImgResponseList(readPlaceImgResponseList)
                    .categoryLists(categoryLists)
                    .build());
        });

        return courseList;
    }

}
