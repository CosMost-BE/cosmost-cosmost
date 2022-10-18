package com.cosmost.project.cosmost.infrastructure.repository;

import com.cosmost.project.cosmost.infrastructure.entity.CategoryListEntity;
import com.cosmost.project.cosmost.infrastructure.entity.CourseEntity;
import com.cosmost.project.cosmost.infrastructure.entity.LocationCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocationCategoryRepository extends JpaRepository<LocationCategoryEntity, Long> {

}