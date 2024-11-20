package com.popcorn.popcorn.domain.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "popup")
public class PopupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long popupid;

    private String title;
    private String contents;
    private Date started_at;
    private Date ended_at;
    private Float latitude; //위도
    private Float longitude; //경도
    private String organizer_url;
    private String reservation_url;
    private Integer categoryid;
    //사진

    public Long getPopupid() {
        return popupid;
    }

    public void setPopupid(Long popupid) {
        this.popupid = popupid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
