package com.cosmost.project.cosmost.infrastructure.entity;

import com.cosmost.project.cosmost.model.Course;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 파라미터가 없는 생성자를 생성
@Table(name = "place_detail")
public class PlaceDetailEntity extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String placeName;

    @NotNull
    private String placeUrl;

    @NotNull
    private int placeOrder;

    @NotNull
    private String placeComment;

    @ManyToOne(fetch = FetchType.LAZY)
    private CourseEntity course;

    @Builder
    public PlaceDetailEntity(Long id, String placeName, String placeUrl, int placeOrder,
                             String placeComment, CourseEntity course) {
        this.id = id;
        this.placeUrl = placeUrl;
        this.placeName = placeName;
        this.placeOrder = placeOrder;
        this.placeComment = placeComment;
        this.course = course;
    }

}
