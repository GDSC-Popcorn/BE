package com.popcorn.popcorn.repository;

import com.popcorn.popcorn.domain.entity.RefreshEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface RefreshRepository extends JpaRepository<RefreshEntity, Long> {

    @Transactional
    void deleteByRefresh(String refresh);

    boolean existsByRefresh(String refresh);
}
