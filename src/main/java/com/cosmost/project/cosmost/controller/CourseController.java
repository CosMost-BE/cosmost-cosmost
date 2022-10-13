package com.cosmost.project.cosmost.controller;

import com.cosmost.project.cosmost.exception.QueryNotfound;
import com.cosmost.project.cosmost.infrastructure.entity.CourseEntity;
import com.cosmost.project.cosmost.requestbody.CreateCourseRequest;
import com.cosmost.project.cosmost.requestbody.UpdateCourseRequest;
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
        cosmostsService.updateCourse(id,updateCourseRequest);

        return ResponseEntity.ok("코스가 수정되었습니다.");
    }

    // 코스 삭제
    @DeleteMapping("/cosmosts/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> deleteCourse(@PathVariable Long id) {
        cosmostsService.deleteCourse(id);

        return ResponseEntity.ok("코스가 삭제되었습니다.");
    }

    // 코스 조회
    @GetMapping("/cosmosts")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> readCourse(@RequestParam(value="filter", required=false) String filter,
                                        @RequestParam(value="limit", required=false) String limit,
                                        @RequestParam(value="category", required=false) String category) {

        if(filter.equals("auth")) {
            List<CourseView> courseView = cosmostsService.readCourseByAuthId();
            return ResponseEntity.status(200).body(courseView);
        } else {
            throw new QueryNotfound();
        }
    }

    // 작성한 코스 목록 조회
    @GetMapping("/cosmosts/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> readCourseByCourseId(@PathVariable Long id) {
        CourseView courseView = cosmostsService.readCourseByCourseId(id);

        return ResponseEntity.status(200).body(courseView);
    }

}
