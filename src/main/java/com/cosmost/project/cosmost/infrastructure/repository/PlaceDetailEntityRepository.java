package com.cosmost.project.cosmost.infrastructure.repository;

import com.cosmost.project.cosmost.infrastructure.entity.CourseEntity;
import com.cosmost.project.cosmost.infrastructure.entity.PlaceDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaceDetailEntityRepository extends JpaRepository<PlaceDetailEntity, Long> {

    List<PlaceDetailEntity> findAllByCourse(CourseEntity courseEntity);
    List<PlaceDetailEntity> findByCourse_Id(Long id);

}