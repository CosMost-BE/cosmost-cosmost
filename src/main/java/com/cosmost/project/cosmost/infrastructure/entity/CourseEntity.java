package com.cosmost.project.cosmost.infrastructure.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 파라미터가 없는 생성자를 생성
@AllArgsConstructor // 클래스에 존재하는 모든 필드에 대한 생성자를 자동으로 생성
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
}
