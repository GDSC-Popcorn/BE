package com.popcorn.popcorn.domain.entity;

import com.popcorn.popcorn.domain.InterestType;
import com.popcorn.popcorn.domain.Role;
import com.popcorn.popcorn.domain.common.BaseEntity;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Builder
public class UserEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //로그인할때 치는 ID를 나타냄
    @Column(nullable = false, unique = true)
    private String username;

    private String password;

    //사람 이름
    private String name;

    private String nickname;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserInterest> interests = new HashSet<>();


    //프로필 사진 0~4번 기본이미지 나타내는 변수
    private Long profileId;


    @Embedded
    private OauthInfo oauthInfo;

    //양방향 관계 매핑이라서 해줘야됨
    public void addUserInterest(UserInterest userInterest){
        if (this.interests == null) {
            this.interests = new HashSet<>();
        }
        this.interests.add(userInterest);
    }

    /*
    /*
        사진은 나중에.
     */



    public void updateUserInfo(String nickname, Long profileId){
        this.nickname = nickname;
        this.profileId = profileId;
    }

}
