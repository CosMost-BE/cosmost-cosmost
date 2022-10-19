package com.cosmost.project.cosmost.infrastructure.repository;

import com.cosmost.project.cosmost.infrastructure.entity.CategoryListEntity;
import com.cosmost.project.cosmost.infrastructure.entity.CourseEntity;
import com.cosmost.project.cosmost.infrastructure.entity.HashtagEntity;
import com.cosmost.project.cosmost.model.CategoryList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryListRepository extends JpaRepository<CategoryListEntity, Long> {
//    List<CategoryListEntity> findAllByCourse(CourseEntity courseEntity);
    List<CategoryListEntity> findByCourse_Id(Long id);

}