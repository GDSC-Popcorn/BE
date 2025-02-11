package com.popcorn.popcorn.domain.entity;

import com.popcorn.popcorn.domain.InterestType;
import com.popcorn.popcorn.domain.Role;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private String name;

    private String nickname;

    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserInterest> interests = new HashSet<>();

    //양방향 관계 매핑이라서 해줘야됨
    public void addUserInterest(UserInterest userInterest){
        if (this.interests == null) {
            this.interests = new HashSet<>();
        }
        this.interests.add(userInterest);
    }
    
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<LikeEntity> likes;

    public Long getUserId(){
        return id;
    }

    /*
    /*
        사진은 나중에.
     */



}
