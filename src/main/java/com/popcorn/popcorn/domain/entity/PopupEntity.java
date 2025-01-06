package com.popcorn.popcorn.domain.entity;

import com.popcorn.popcorn.domain.InterestType;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PopupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long popupId;

    private String title;
    private String contents;
    private Date startedAt;
    private Date endedAt;
    private String hours;
    private Float latitude; //위도
    private Float longitude; //경도
    private String location;
    private String organizerUrl;
    private String reservationUrl;
    private String popupImage;
    private Boolean confirm;

    @Builder
    public PopupEntity(String title, String contents, Date startedAt, Date endedAt) {
        this.title = title;
        this.contents = contents;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
    }

    @OneToMany(mappedBy = "popup",cascade = CascadeType.ALL)
    private List<LikeEntity> likes;

    @ElementCollection(targetClass = InterestType.class)
    @Enumerated(EnumType.STRING)
    private List<InterestType> categories;

}
