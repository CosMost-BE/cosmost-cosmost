package com.cosmost.project.cosmost.infrastructure.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "hashtag")
public class HashtagEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String keyword;
    @ManyToOne(fetch = FetchType.LAZY)
    private CourseEntity course;

    @Builder
    public HashtagEntity(Long id, String keyword, CourseEntity course) {
        this.id = id;
        this.keyword = keyword;
        this.course = course;
    }

}
