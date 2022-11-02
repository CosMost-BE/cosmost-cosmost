package com.cosmost.project.cosmost.controller;

import com.cosmost.project.cosmost.exception.QueryNotfound;
import com.cosmost.project.cosmost.infrastructure.entity.CourseEntity;
import com.cosmost.project.cosmost.infrastructure.entity.LocationCategoryEntity;
import com.cosmost.project.cosmost.infrastructure.entity.ThemeCategoryEntity;
import com.cosmost.project.cosmost.model.Course;
import com.cosmost.project.cosmost.requestbody.CreateCourseRequest;
import com.cosmost.project.cosmost.requestbody.UpdateCourseRequest;
import com.cosmost.project.cosmost.responsebody.ReadCourseResponse;
import com.cosmost.project.cosmost.service.CategoryService;
import com.cosmost.project.cosmost.service.CosmostsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/v1/cosmosts")
public class CourseController {

    private final CosmostsService cosmostsService;
    private final CategoryService categoryService;

    @Autowired
    public CourseController(CosmostsService cosmostsService, CategoryService categoryService) {
        this.cosmostsService = cosmostsService;
        this.categoryService = categoryService;
    }

    // 코스 등록
    @PostMapping("")
    public ResponseEntity<String> createCourse(@Valid @RequestPart CreateCourseRequest createCourseRequest,
                                          @RequestPart(value="file", required = false) List<MultipartFile> file) {

        cosmostsService.createCourse(createCourseRequest, file);

        return ResponseEntity.ok("코스가 추가되었습니다.");
    }

    // 코스 수정
    @PutMapping("/{id}")
    public ResponseEntity<String> updateCourse(@PathVariable Long id,
                                          @Valid @RequestPart UpdateCourseRequest updateCourseRequest,
                                          @RequestPart(value="file", required = false) List<MultipartFile> file) {

        cosmostsService.updateCourse(id,updateCourseRequest, file);

        return ResponseEntity.ok("코스가 수정되었습니다.");
    }

    // 코스 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCourse(@PathVariable Long id) {
        cosmostsService.deleteCourse(id);

        return ResponseEntity.ok("코스가 삭제되었습니다.");
    }

    // 코스 조회
    @GetMapping("")
    public ResponseEntity<?> readCourse(@RequestParam(value="filter", defaultValue = " ", required=false) String filter,
                                        @RequestParam(value="category", defaultValue = " ", required=false) String category,
                                        @RequestParam(value="name-id", defaultValue = " ", required=false) Long nameId,
                                        @RequestParam(value="keyword", defaultValue = " ", required=false) String keyword,
                                        @RequestParam(value="hashtag", defaultValue = " ", required=false) String hashtag,
                                        Pageable pageable) {

        System.out.println("#$%#$%#$%#$%#$"+hashtag);

        if(String.valueOf(filter).equals("auth")) { // 작성한 코스 목록 조회
            List<ReadCourseResponse> course = cosmostsService.readCourseByAuthId(pageable);
            return ResponseEntity.ok(course);
        } else if (String.valueOf(filter).equals("all") && String.valueOf(category).equals("location")) { // 지역별 카테고리 전체 조회
            List<LocationCategoryEntity> locationCategoryEntityList = categoryService.readAllLocationCategory();
            return ResponseEntity.ok(locationCategoryEntityList);
        } else if (String.valueOf(filter).equals("all") && String.valueOf(category).equals("theme")) { // 테마별 카테고리 전체 조회
            List<ThemeCategoryEntity> themeCategoryEntityList = categoryService.readAllThemeCategory();
            return ResponseEntity.ok(themeCategoryEntityList);
        } else if (String.valueOf(filter).equals("all")) { // 코스 전체 목록 조회
            List<ReadCourseResponse> course = cosmostsService.readCourseAll(pageable);
            return ResponseEntity.ok(course);
        } else if (String.valueOf(category).equals("location") && !String.valueOf(nameId).equals(" ") ||
                String.valueOf(category).equals("theme") && !String.valueOf(nameId).equals(" ")) { // 카테고리로 코스 전체 목록 조회
            List<ReadCourseResponse> course = cosmostsService.readCourseByCategoryAll(category, nameId, pageable);
            return ResponseEntity.ok(course);
        }else if (!String.valueOf(hashtag).equals(" ")) { // 해시태그별로 코스 조회
            List<ReadCourseResponse> course = cosmostsService.readCourseByHashtag(hashtag, pageable);
            return ResponseEntity.ok(course);
        } else if (!String.valueOf(keyword).equals(" ")) { // 코스 검색 목록 조회
            List<ReadCourseResponse> course = cosmostsService.readCourseByKeyword(keyword, pageable);
            return ResponseEntity.ok(course);
        } else {
            throw new QueryNotfound();
        }
    }

    // 코스 한 개 조회
    @GetMapping("/{id}")
    public ResponseEntity<?> readCourseByCourseId(@PathVariable Long id,
                                                  @RequestParam(value="filter", required=false) String filter) {
        if(String.valueOf(filter).equals("frame")) { // 코스 한 개 목록 조회
            ReadCourseResponse readCourseResponse = cosmostsService.readCourseFrameByCourseId(id);
            return ResponseEntity.ok(readCourseResponse);
        } else {
            Course course = cosmostsService.readCourseByCourseId(id); // 코스 한 개 상세 조회
            return ResponseEntity.ok(course);
        }

    }



}
