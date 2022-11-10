package com.cosmost.project.cosmost.infrastructure.repository;

import com.cosmost.project.cosmost.infrastructure.entity.CategoryListEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryListRepository extends JpaRepository<CategoryListEntity, Long> {
    List<CategoryListEntity> findByCourse_Id(Long id);
    Slice<CategoryListEntity> findAllByLocationCategory_Id(Long id, Pageable pageable);
    Slice<CategoryListEntity> findAllByThemeCategory_Id(Long id, Pageable pageable);

}