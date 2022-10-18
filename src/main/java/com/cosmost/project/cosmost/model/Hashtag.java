package com.cosmost.project.cosmost.model;

import com.cosmost.project.cosmost.infrastructure.entity.HashtagEntity;
import com.cosmost.project.cosmost.infrastructure.entity.PlaceDetailEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;


@Getter
@ToString
@Builder
@AllArgsConstructor
public class Hashtag {

    private Long id;

    private String keyword;

    public Hashtag(HashtagEntity hashtagEntity) {
        this.id = hashtagEntity.getId();
        this.keyword = hashtagEntity.getKeyword();
    }

}
