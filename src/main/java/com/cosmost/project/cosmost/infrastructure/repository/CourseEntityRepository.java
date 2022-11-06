package com.cosmost.project.cosmost.infrastructure.repository;

import com.cosmost.project.cosmost.infrastructure.entity.CourseEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseEntityRepository extends JpaRepository<CourseEntity, Long> {

    Slice<CourseEntity> findAllByAuthorId(Long authorId, Pageable pageable);
    int countByAuthorId(Long authorId);

    Optional<CourseEntity> findAllByIdAndAuthorId(Long id, Long authorId);
}