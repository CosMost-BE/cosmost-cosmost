package com.cosmost.project.cosmost.service;

import com.cosmost.project.cosmost.infrastructure.entity.*;
import com.cosmost.project.cosmost.infrastructure.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

    private final LocationCategoryRepository locationCategoryRepository;

    @Autowired
    public CategoryServiceImpl(LocationCategoryRepository locationCategoryRepository) {
        this.locationCategoryRepository = locationCategoryRepository;

    }

    // 지역별 카테고리 전체 조회
    @Override
    public List<LocationCategoryEntity> readAllLocationCategory() {
        return locationCategoryRepository.findAll();
    }
}
