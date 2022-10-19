package com.cosmost.project.cosmost.infrastructure.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 파라미터가 없는 생성자를 생성
@Table(name = "place_detail")
public class PlaceDetailEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String placeName;

    @NotNull
    @Column(name = "place_x_coordinate")
    private double placeXCoordinate;

    @NotNull
    @Column(name = "place_y_coordinate")
    private double placeYCoordinate;

    @NotNull
    private int placeOrder;

    @NotNull
    private String placeComment;

    @ManyToOne(fetch = FetchType.LAZY)
    private CourseEntity course;

    @Builder
    public PlaceDetailEntity(Long id, String placeName, double placeXCoordinate, double placeYCoordinate,
                             int placeOrder, String placeComment, CourseEntity course) {
        this.id = id;
        this.placeName = placeName;
        this.placeXCoordinate = placeXCoordinate;
        this.placeYCoordinate = placeYCoordinate;
        this.placeOrder = placeOrder;
        this.placeComment = placeComment;
        this.course = course;
    }

}
