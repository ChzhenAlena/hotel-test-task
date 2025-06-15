package com.example.hotel.repository;

import com.example.hotel.entity.Hotel;
import com.example.hotel.entity.projection.HotelProjection;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HotelRepository
        extends JpaRepository<Hotel, Long>, JpaSpecificationExecutor<Hotel>, HotelAnalyticsRepository {
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
          from Hotel h
          left join Address a on a.hotel.id = h.id
          left join Contacts c on c.hotel.id = h.id
    """)
    List<HotelProjection> findAllHotelProjections();


    @EntityGraph(attributePaths = {"address", "contacts", "arrivalTime", "amenities"})
    Optional<Hotel> findById(Long id);

    @EntityGraph(attributePaths = {
            "address",
            "contacts",
            "arrivalTime"
    })
    List<Hotel> findAll(Specification<Hotel> spec);

    @EntityGraph(attributePaths = {
            "address",
            "contacts",
            "arrivalTime"
    })
    List<Hotel> findAll();
}
