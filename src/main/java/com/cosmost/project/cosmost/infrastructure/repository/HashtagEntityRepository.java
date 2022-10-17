package com.cosmost.project.cosmost.infrastructure.repository;

import com.cosmost.project.cosmost.infrastructure.entity.CourseEntity;
import com.cosmost.project.cosmost.infrastructure.entity.HashtagEntity;
import com.cosmost.project.cosmost.infrastructure.entity.PlaceDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HashtagEntityRepository extends JpaRepository<HashtagEntity, Long> {

    List<HashtagEntity> findAllByCourse(CourseEntity courseEntity);
    List<HashtagEntity> findByCourse_Id(Long id);

}