package com.popcorn.popcorn.domain.dto;

import com.popcorn.popcorn.domain.InterestType;
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
    private InterestType interest;

    @Builder
    public HomeDto(Long id, String title, String popupImage, Date startedAt, Date endedAt, String location, InterestType interest) {
        this.popupId = id;
        this.title = title;
        this.popupImage = popupImage;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.location = location;
        this.interest = interest;
    }
}


