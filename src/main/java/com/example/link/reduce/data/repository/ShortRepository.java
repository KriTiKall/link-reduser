package com.example.link.reduce.data.repository;

import com.example.link.reduce.data.entity.ShortEntity;
import org.springframework.data.repository.CrudRepository;

public interface ShortRepository extends CrudRepository<ShortEntity, Long> {

    void deleteById(Long id);
}
