package com.cosmost.project.cosmost.requestbody;

import com.cosmost.project.cosmost.infrastructure.entity.CourseEntity;
import com.cosmost.project.cosmost.infrastructure.entity.HashtagEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateHashtagRequest {
    private Long id;

    @NotBlank(message = "키워드는 필수 입력 값입니다.")
    private String keyword; // 키워드

    private Long course;

    public HashtagEntity updateDtoToEntity(UpdateHashtagRequest updateHashtagRequest, CourseEntity courseEntity) {
        return HashtagEntity.builder()
                .id(updateHashtagRequest.getId())
                .keyword(updateHashtagRequest.getKeyword())
                .course(courseEntity)
                .build();
    }
}

