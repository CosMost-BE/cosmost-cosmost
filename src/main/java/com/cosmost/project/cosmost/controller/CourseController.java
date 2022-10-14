package com.cosmost.project.cosmost.controller;

import com.cosmost.project.cosmost.exception.QueryNotfound;
import com.cosmost.project.cosmost.infrastructure.entity.CourseEntity;
import com.cosmost.project.cosmost.model.Course;
import com.cosmost.project.cosmost.requestbody.CreateCourseRequest;
import com.cosmost.project.cosmost.requestbody.UpdateCourseRequest;
import com.cosmost.project.cosmost.responsebody.ReadCourseResponse;
import com.cosmost.project.cosmost.service.CosmostsService;
import com.cosmost.project.cosmost.view.CourseView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/v1/cosmosts")
public class CourseController {

    private final CosmostsService cosmostsService;

    @Autowired
    public CourseController(CosmostsService cosmostsService) {
        this.cosmostsService = cosmostsService;
    }

    // 코스 등록
    @PostMapping("")
    public ResponseEntity<?> createCourse(@Valid @RequestBody CreateCourseRequest createCourseRequest) {
        cosmostsService.createCourse(createCourseRequest);

        return ResponseEntity.ok("코스가 추가되었습니다.");
    }

    // 코스 수정
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCourse(@PathVariable Long id,
                                          @Valid @RequestBody UpdateCourseRequest updateCourseRequest) {
        cosmostsService.updateCourse(id,updateCourseRequest);

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
            List<Course> course = cosmostsService.readCourseByAuthId();
            return ResponseEntity.status(200).body(course);
        } else {
            throw new QueryNotfound();
        }
    }

    // 코스 한 개 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<?> readCourseByCourseId(@PathVariable Long id) {
        ReadCourseResponse readCourseResponse = cosmostsService.readCourseByCourseId(id);

        return ResponseEntity.status(200).body(readCourseResponse);
    }

}
