package com.example.hotel.infrastructure.jpa.repository;

import com.example.hotel.domain.projection.HotelProjection;
import com.example.hotel.infrastructure.jpa.entity.HotelEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HotelJpaRepository extends JpaRepository<HotelEntity, Long>, JpaSpecificationExecutor<HotelEntity> {
    @Query("""
                select h.id as id,
                       h.name as name,
                       h.description as description,
                       a.houseNumber as houseNumber,
                       a.street as street,
                       a.city as city,
                       a.postCode as postCode,
                       a.country as country,
                       c.phone as phone
                  from HotelEntity h
                  left join AddressEntity a on a.hotel.id = h.id
                  left join ContactsEntity c on c.hotel.id = h.id
            """)
    List<HotelProjection> findAllHotelProjections();


    @EntityGraph(attributePaths = {"address", "contacts", "arrivalTime", "amenities"})
    Optional<HotelEntity> findById(Long id);

    @EntityGraph(attributePaths = {
            "address",
            "contacts",
            "arrivalTime"
    })
    List<HotelEntity> findAll(Specification<HotelEntity> spec);
}
