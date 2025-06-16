package com.example.hotel.infrastructure.jpa.repository;

import com.example.hotel.infrastructure.jpa.entity.AmenityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AmenityJpaRepository extends JpaRepository<AmenityEntity, Long> {
    List<AmenityEntity> findByNameInIgnoreCase(List<String> names);
}



