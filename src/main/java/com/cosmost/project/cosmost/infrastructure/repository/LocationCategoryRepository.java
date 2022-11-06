package com.cosmost.project.cosmost.infrastructure.repository;

import com.cosmost.project.cosmost.infrastructure.entity.LocationCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LocationCategoryRepository extends JpaRepository<LocationCategoryEntity, Long> {

}