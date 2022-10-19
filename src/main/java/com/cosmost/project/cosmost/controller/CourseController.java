package com.cosmost.project.cosmost.controller;

import com.cosmost.project.cosmost.exception.QueryNotfound;
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
    public ResponseEntity<?> createCourse(@Valid @RequestPart CreateCourseRequest createCourseRequest,
                                          @RequestPart(value="file", required = false) List<MultipartFile> file) {

        cosmostsService.createCourse(createCourseRequest, file);

        return ResponseEntity.ok("코스가 추가되었습니다.");
    }

    // 코스 수정
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCourse(@PathVariable Long id,
                                          @Valid @RequestPart UpdateCourseRequest updateCourseRequest,
                                          @RequestPart(value="file", required = false) List<MultipartFile> file) {
        cosmostsService.updateCourse(id,updateCourseRequest, file);

        return ResponseEntity.ok("코스가 수정되었습니다.");
    }

    // 코스 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable Long id) {
        cosmostsService.deleteCourse(id);

        return ResponseEntity.ok("코스가 삭제되었습니다.");
    }

    // 코스 조회
    @GetMapping("")
    public ResponseEntity<?> readCourse(@RequestParam(value="filter", required=false) String filter,
                                        @RequestParam(value="limit", required=false) String limit,
                                        @RequestParam(value="category", required=false) String category) {

        if(String.valueOf(filter).equals("auth")) {
            List<ReadCourseResponse> course = cosmostsService.readCourseByAuthId();
            return ResponseEntity.status(200).body(course);
        } else if (String.valueOf(filter).equals("all") && String.valueOf(category).equals("location")) {
            List<LocationCategoryEntity> locationCategoryEntityList = categoryService.readAllLocationCategory();
            return ResponseEntity.status(200).body(locationCategoryEntityList);
        }  else if (String.valueOf(filter).equals("all") && String.valueOf(category).equals("theme")) {
            List<ThemeCategoryEntity> themeCategoryEntityList = categoryService.readAllThemeCategory();
            return ResponseEntity.status(200).body(themeCategoryEntityList);
        } else {
            throw new QueryNotfound();
        }
    }


    // 코스 한 개 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<?> readCourseByCourseId(@PathVariable Long id) {
        Course course = cosmostsService.readCourseByCourseId(id);

        return ResponseEntity.status(200).body(course);
    }



}
