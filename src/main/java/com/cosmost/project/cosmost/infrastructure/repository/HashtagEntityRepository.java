package com.cosmost.project.cosmost.infrastructure.repository;

import com.cosmost.project.cosmost.infrastructure.entity.CourseEntity;
import com.cosmost.project.cosmost.infrastructure.entity.HashtagEntity;
import com.cosmost.project.cosmost.infrastructure.entity.PlaceDetailEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HashtagEntityRepository extends JpaRepository<HashtagEntity, Long> {

    List<HashtagEntity> findAllByCourse(CourseEntity courseEntity);
    List<HashtagEntity> findByCourse_Id(Long id);

    @Query(value = "select h from HashtagEntity h join fetch h.course " +
            "where h.course.courseTitle like %:keyword% or h.course.courseComment like %:keyword% or h.keyword like %:keyword% " +
            "group by h.course")
    Slice<HashtagEntity> searchCourse(@Param("keyword") String keyword, Pageable pageable);

}