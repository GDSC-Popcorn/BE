package com.popcorn.popcorn.domain.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Builder
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL) //null값은 JSON 응답에서 제외
public class PopupDetailDto {
    private Long popupId;
    private String title;
    private Date startedAt;
    private Date endedAt;
    private String hours;
    private String contents;
    private String location;
    private String organizerUrl;
    private String reservationUrl;
    private List<String> popupImage;
    private Boolean isLiked;

    @Builder
    public PopupDetailDto(Long id, String title, Date startedAt, Date endedAt,
                          String hours, String contents, String location,
                          String organizerUrl, String reservationUrl, List<String> popupImage, Boolean isLiked) {
        this.popupId = id;
        this.title = title;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.hours = hours;
        this.contents = contents;
        this.location = location;
        this.organizerUrl = organizerUrl;
        this.reservationUrl = reservationUrl;
        this.popupImage = popupImage;
        this.isLiked = isLiked;
    }

}
