package com.popcorn.popcorn.domain.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.popcorn.popcorn.domain.InterestType;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) //null값은 JSON 응답에서 제외
public class PopupDetailDto {
    private Long popupId;
    private String title;
    private InterestType interest;
    private Date startedAt;
    private Date endedAt;
    private String business_hours;
    private String content;
    private String location;
    private String organizerUrl;
    private String reservationUrl;
    private List<String> popupImage;
    private Boolean isLiked;

}
