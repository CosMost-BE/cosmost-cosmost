package com.cosmost.project.cosmost.responsebody;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReadPlaceImgResponse {

    private Long id;

    private int placeImgOrder;
    private String placeImgUrl;

}
