package com.cosmost.project.cosmost.service;

import com.cosmost.project.cosmost.infrastructure.entity.LocationCategoryEntity;
import com.cosmost.project.cosmost.infrastructure.entity.ThemeCategoryEntity;

import java.util.List;

public interface CategoryService {
    List<LocationCategoryEntity> readAllLocationCategory();
    List<ThemeCategoryEntity> readAllThemeCategory();

}
