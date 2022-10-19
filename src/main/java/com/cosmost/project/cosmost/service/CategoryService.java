package com.cosmost.project.cosmost.service;

import com.cosmost.project.cosmost.infrastructure.entity.LocationCategoryEntity;
import com.cosmost.project.cosmost.infrastructure.entity.ThemeCategoryEntity;
import com.cosmost.project.cosmost.model.Course;
import com.cosmost.project.cosmost.requestbody.CreateCourseRequest;
import com.cosmost.project.cosmost.requestbody.UpdateCourseRequest;
import com.cosmost.project.cosmost.responsebody.ReadCourseResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CategoryService {
    List<LocationCategoryEntity> readAllLocationCategory();

}
