package com.popcorn.popcorn.repository;

import com.popcorn.popcorn.domain.InterestType;
import com.popcorn.popcorn.domain.entity.UserInterest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserInterestRepository extends JpaRepository<UserInterest, Long> {
    //사용자 관심사 가져오기
    @Query("SELECT ui.interest FROM UserInterest ui WHERE ui.userEntity.id = :userId")
    List<InterestType> findInterestsByUserId(@Param("userId") Long userId);
}
