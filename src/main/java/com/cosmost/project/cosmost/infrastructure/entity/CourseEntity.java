package com.cosmost.project.cosmost.infrastructure.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 파라미터가 없는 생성자를 생성
@Table(name = "course")
public class CourseEntity extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long authorId;

    @NotNull
    private String courseTitle;

    @NotNull
    private String courseComment;

    @Enumerated(EnumType.STRING)
    private CourseStatus courseStatus;

    @Builder
    public CourseEntity(Long id, Long authorId,
                        String courseTitle, String courseComment, CourseStatus courseStatus) {
        this.id = id;
        this.authorId = authorId;
        this.courseTitle = courseTitle;
        this.courseComment = courseComment;
        this.courseStatus = courseStatus;
    }

}
