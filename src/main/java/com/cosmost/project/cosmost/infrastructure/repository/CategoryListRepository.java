package com.cosmost.project.cosmost.infrastructure.repository;

import com.cosmost.project.cosmost.infrastructure.entity.CategoryListEntity;
import com.cosmost.project.cosmost.infrastructure.entity.CourseEntity;
import com.cosmost.project.cosmost.infrastructure.entity.HashtagEntity;
import com.cosmost.project.cosmost.model.CategoryList;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryListRepository extends JpaRepository<CategoryListEntity, Long> {
    List<CategoryListEntity> findByCourse_Id(Long id);
    Slice<CategoryListEntity> findAllByLocationCategory_Id(Long id, Pageable pageable);
    Slice<CategoryListEntity> findAllByThemeCategory_Id(Long id, Pageable pageable);

}