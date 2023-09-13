package com.example.link.reduce.data.repository;

import com.example.link.reduce.data.entity.LinkEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface LinkRepository extends CrudRepository<LinkEntity, Long> {

    @Query(value = "select count(l) from LinkEntity l where l.userId = :id")
    Long countByUserId(@Param("id") Long id);

    @Query(value = "select l from LinkEntity l where l.userId = :id")
    List<LinkEntity> findByUserId(@Param("id") Long id);

    @Query(value = "select l from LinkEntity l where l.userId = :id and l.name = :name")
    LinkEntity findByUserIdAndName(@Param("id") Long id, @Param("name") String name);

    @Modifying
    @Transactional
    @Query(value = "delete from LinkEntity l where l.userId = :id and l.name = :name")
    void deleteByUserIdAndName(@Param("id") Long id, @Param("name") String name);

    @Query(value = "select l from LinkEntity l")
    List<LinkEntity> findAll();
}
