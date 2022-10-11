package com.cosmost.project.cosmost.controller;

import com.cosmost.project.cosmost.requestbody.CreateCourseRequest;
import com.cosmost.project.cosmost.requestbody.UpdateCourseRequest;
import com.cosmost.project.cosmost.service.CosmostsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/v1")
public class CourseController {

    private final CosmostsService cosmostsService;

    @Autowired
    public CourseController(CosmostsService cosmostsService) {
        this.cosmostsService = cosmostsService;
    }

    // 코스 등록
    @PostMapping("/cosmosts")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createCourse(@Valid @RequestBody CreateCourseRequest createCourseRequest) {
        cosmostsService.createCourse(createCourseRequest);

        return ResponseEntity.ok("코스가 추가되었습니다.");
    }

    // 코스 수정
    @PutMapping("/cosmosts/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> updateCourse(@PathVariable Long id,
                                          @Valid @RequestBody UpdateCourseRequest updateCourseRequest) {
        cosmostsService.updateCourse(updateCourseRequest);

        return ResponseEntity.ok("코스가 수정되었습니다.");
    }

}
