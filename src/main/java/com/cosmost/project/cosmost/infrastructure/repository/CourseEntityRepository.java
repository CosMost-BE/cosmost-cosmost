package com.cosmost.project.cosmost.infrastructure.repository;

import com.cosmost.project.cosmost.infrastructure.entity.CourseEntity;
import com.cosmost.project.cosmost.infrastructure.entity.HashtagEntity;
import com.cosmost.project.cosmost.model.Course;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

public interface CourseEntityRepository extends JpaRepository<CourseEntity, Long> {

    Slice<CourseEntity> findAllByAuthorId(Long authorId, Pageable pageable);


//    @Query(value = "select count(authorId) from CourseEntity where authorId = :authorId")
//    int authorCourseCount(Long authorId);

    int countByAuthorId(Long authorId);
}