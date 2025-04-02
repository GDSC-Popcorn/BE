package com.popcorn.popcorn.repository;

import com.popcorn.popcorn.domain.entity.OauthInfo;
import com.popcorn.popcorn.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByUsername(String username);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    UserEntity findByEmail(String email);

    UserEntity findByNameAndEmail(String name, String email);

    Optional<UserEntity> findByOauthInfo(OauthInfo oauthInfo);
}
