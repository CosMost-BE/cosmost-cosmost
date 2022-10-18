package com.cosmost.project.cosmost.infrastructure.repository;

import com.cosmost.project.cosmost.infrastructure.entity.CourseEntity;
import com.cosmost.project.cosmost.infrastructure.entity.LocationCategoryEntity;
import com.cosmost.project.cosmost.infrastructure.entity.ThemeCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ThemeCategoryRepository extends JpaRepository<ThemeCategoryEntity, Long> {

}