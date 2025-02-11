package com.popcorn.popcorn.domain.dto;

import lombok. *;

import java.util.Date;

@Builder
@Getter
public class HomeDto {
    private Long popupId;
    private String title;
    private String popupImage;
    private Date startedAt;
    private Date endedAt;
    private String location;

    @Builder
    public HomeDto(Long id, String title, String popupImage, Date startedAt, Date endedAt, String location) {
        this.popupId = id;
        this.title = title;
        this.popupImage = popupImage;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.location = location;
    }
}


