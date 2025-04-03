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
@Table(name = "pop_up")
public class PopupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    private String title;
    @Column(length = 2000)
    private String content;
    private Date startedAt;
    private Date endedAt;
    private String business_hours;
    private Float latitude; //위도
    private Float longitude; //경도
    private String location;
    private String organizerUrl;
    private String reservationUrl;
    private Boolean confirmed;

    @OneToMany(mappedBy = "popup",cascade = CascadeType.ALL)
    private List<LikeEntity> likes;

    @Enumerated(EnumType.STRING)
    private InterestType interest;
}
