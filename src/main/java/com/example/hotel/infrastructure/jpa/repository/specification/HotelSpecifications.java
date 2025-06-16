package com.example.hotel.infrastructure.jpa.repository.specification;

import com.example.hotel.infrastructure.jpa.entity.AddressEntity;
import com.example.hotel.infrastructure.jpa.entity.AmenityEntity;
import com.example.hotel.infrastructure.jpa.entity.HotelEntity;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.util.Set;

public class HotelSpecifications {

    public static Specification<HotelEntity> hasName(String name) {
        return (root, query, cb) ->
                (name == null || name.isBlank()) ? null :
                        cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase().trim() + "%");
    }

    public static Specification<HotelEntity> hasBrand(String brand) {
        return (root, query, cb) ->
                (brand == null || brand.isBlank()) ? null :
                        cb.equal(cb.lower(root.get("brand")), brand.toLowerCase().trim());
    }

    public static Specification<HotelEntity> hasCity(String city) {
        return (root, query, cb) -> {
            if (city == null || city.isBlank()) {
                return null;
            }
            Join<HotelEntity, AddressEntity> addressJoin = root.join("address", JoinType.LEFT);
            return cb.equal(cb.lower(addressJoin.get("city")), city.toLowerCase().trim());
        };
    }

    public static Specification<HotelEntity> hasCountry(String country) {
        return (root, query, cb) -> {
            if (country == null || country.isBlank()) {
                return null;
            }
            Join<HotelEntity, AddressEntity> addressJoin = root.join("address", JoinType.LEFT);
            return cb.equal(cb.lower(addressJoin.get("country")), country.toLowerCase().trim());
        };
    }

    public static Specification<HotelEntity> hasAllAmenities(Set<String> amenities) {
        return (root, query, cb) -> {
            if (amenities == null || amenities.isEmpty()) {
                return cb.conjunction();
            }

            Subquery<Long> subquery = query.subquery(Long.class);
            Root<HotelEntity> subRoot = subquery.from(HotelEntity.class);
            Join<HotelEntity, AmenityEntity> amenityJoin = subRoot.join("amenities");

            subquery.select(cb.countDistinct(amenityJoin.get("id")));
            subquery.where(
                    cb.equal(subRoot.get("id"), root.get("id")),
                    amenityJoin.get("name").in(amenities)
            );

            return cb.equal(subquery, (long) amenities.size());
        };
    }

    public static Specification<HotelEntity> fetchAssociations() {
        return (root, query, cb) -> {
            if (HotelEntity.class.equals(query.getResultType())) {
                root.fetch("address", JoinType.LEFT);
                root.fetch("contacts", JoinType.LEFT);
                root.fetch("arrivalTime", JoinType.LEFT);
                root.fetch("amenities", JoinType.LEFT);
                query.distinct(true);
            }
            return cb.conjunction();
        };
    }
}
