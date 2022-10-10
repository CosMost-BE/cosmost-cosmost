package com.cosmost.project.cosmost.infrastructure.repository;

import com.cosmost.project.cosmost.infrastructure.entity.CourseEntity;
import com.cosmost.project.cosmost.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseEntityRepository extends JpaRepository<CourseEntity, Long> {

    List<Course> findAllByAuthorId(Long authorId);
}