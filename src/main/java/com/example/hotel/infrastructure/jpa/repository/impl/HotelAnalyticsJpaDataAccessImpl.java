package com.example.hotel.infrastructure.jpa.repository.impl;

import com.example.hotel.domain.repository.HotelAnalyticsDataAccess;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class HotelAnalyticsJpaDataAccessImpl implements HotelAnalyticsDataAccess {
    private final EntityManager em;

    @Override
    public Map<String, Long> countGroupedByBrand() {
        String queryStr = "SELECT h.brand, COUNT(h) FROM HotelEntity h GROUP BY h.brand";
        return getResultMap(em.createQuery(queryStr, Object[].class));
    }

    @Override
    public Map<String, Long> countGroupedByCity() {
        String queryStr = "SELECT a.city, COUNT(h) FROM HotelEntity h JOIN h.address a GROUP BY a.city";
        return getResultMap(em.createQuery(queryStr, Object[].class));
    }

    @Override
    public Map<String, Long> countGroupedByCountry() {
        String queryStr = "SELECT a.country, COUNT(h) FROM HotelEntity h JOIN h.address a GROUP BY a.country";
        return getResultMap(em.createQuery(queryStr, Object[].class));
    }

    @Override
    public Map<String, Long> countGroupedByAmenities() {
        String queryStr = "SELECT am.name, COUNT(h) FROM HotelEntity h JOIN h.amenities am GROUP BY am.name";
        return getResultMap(em.createQuery(queryStr, Object[].class));
    }

    private Map<String, Long> getResultMap(TypedQuery<Object[]> query) {
        return query.getResultList().stream()
                .collect(Collectors.toMap(
                        row -> (String) row[0],
                        row -> (Long) row[1]
                ));
    }
}
