package com.popcorn.popcorn.domain.entity;

import com.popcorn.popcorn.domain.InterestType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class UserInterest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_entity_id")
    private UserEntity userEntity;

    @Enumerated(EnumType.STRING)
    private InterestType interest;

    public void setUserAndInterest(UserEntity userEntity, InterestType interestType){
        this.userEntity = userEntity;
        this.interest = interestType;
    }

}
