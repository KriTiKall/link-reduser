package com.example.link.reduce.data.repository;

import com.example.link.reduce.data.entity.LinkEntity;
import com.example.link.reduce.data.entity.StatisticEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StatRepository extends CrudRepository<StatisticEntity, Long> {

    @Query(value = "select count(s) from StatisticEntity s where s.linkEntity = :entity")
    Long countByLinkEntity(@Param("entity")LinkEntity entity);
    List<StatisticEntity> findAllByLinkEntity(LinkEntity entity);

    @Modifying
    @Transactional
    void deleteByLinkEntity(LinkEntity entity);
}
