package com.example.link.reduce.data.repository;

import com.example.link.reduce.data.entity.LinkEntity;
import com.example.link.reduce.data.entity.ShortEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ShortRepository extends CrudRepository<ShortEntity, Long> {

    @Query(value = "select s from ShortEntity s where s.shortLink = :code")
    ShortEntity findByCode(@Param("code") String code);

    void deleteById(Long id);
}
