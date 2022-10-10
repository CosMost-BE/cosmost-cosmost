package com.cosmost.project.cosmost.controller;

import com.cosmost.project.cosmost.infrastructure.entity.CourseEntity;
import com.cosmost.project.cosmost.requestbody.CreateCourseRequest;
import com.cosmost.project.cosmost.service.CosmostsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/v1")
@RequiredArgsConstructor
public class CourseController {

    private final CosmostsService cosmostsService;

    // 코스 등록
    @PostMapping("/cosmosts")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createLocationCategory(@Valid @RequestBody CreateCourseRequest createCourseRequest) {
        cosmostsService.createCourse(createCourseRequest);

        return ResponseEntity.ok("코스가 추가되었습니다.");
    }

}
