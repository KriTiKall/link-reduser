package com.example.link.reduce.data.repository;

import com.example.link.reduce.data.entity.LinkEntity;
import com.example.link.reduce.data.entity.StatisticEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

public interface StatRepository extends CrudRepository<StatisticEntity, Long> {

    StatisticEntity findAllByLinkEntity(LinkEntity entity);

    @Modifying
    @Transactional
    void deleteByLinkEntity(LinkEntity entity);
}
