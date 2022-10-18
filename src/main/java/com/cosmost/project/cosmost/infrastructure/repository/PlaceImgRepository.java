package com.cosmost.project.cosmost.infrastructure.repository;

import com.cosmost.project.cosmost.infrastructure.entity.CourseEntity;
import com.cosmost.project.cosmost.infrastructure.entity.PlaceDetailEntity;
import com.cosmost.project.cosmost.infrastructure.entity.PlaceImgEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaceImgRepository extends JpaRepository<PlaceImgEntity, Long> {

    List<PlaceImgEntity> findAllByCourse(CourseEntity courseEntity);
    List<PlaceImgEntity> findByCourse_Id(Long id);

}