package com.popcorn.popcorn.domain.dto;

import com.popcorn.popcorn.domain.InterestType;
import lombok. *;

import java.util.Date;

@AllArgsConstructor
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

}


