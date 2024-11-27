package com.popcorn.popcorn.domain.dto;

import lombok. *;

@Getter
public class HomeDto {
    private Long popupId;
    private String title;
    private String popupImage;
    private int dDay;

    public HomeDto(Long popupId, String title, String popupImage, int dDay) {
        this.popupId = popupId;
        this.title = title;
        this.popupImage = popupImage;
        this.dDay = dDay;
    }
}


