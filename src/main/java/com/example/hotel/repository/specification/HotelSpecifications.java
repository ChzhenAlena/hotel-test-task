package com.example.hotel.repository.specification;

import com.example.hotel.entity.Address;
import com.example.hotel.entity.Amenity;
import com.example.hotel.entity.Hotel;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.util.Set;

public class HotelSpecifications {

    public static Specification<Hotel> hasName(String name) {
        return (root, query, cb) ->
                (name == null || name.isBlank()) ? null :
                        cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase().trim() + "%");
    }

    public static Specification<Hotel> hasBrand(String brand) {
        return (root, query, cb) ->
                (brand == null || brand.isBlank()) ? null :
                        cb.equal(cb.lower(root.get("brand")), brand.toLowerCase().trim());
    }

    public static Specification<Hotel> hasCity(String city) {
        return (root, query, cb) -> {
            if (city == null || city.isBlank()) {
                return null;
            }
            Join<Hotel, Address> addressJoin = root.join("address", JoinType.LEFT);
            return cb.equal(cb.lower(addressJoin.get("city")), city.toLowerCase().trim());
        };
    }

    public static Specification<Hotel> hasCountry(String country) {
        return (root, query, cb) -> {
            if (country == null || country.isBlank()) {
                return null;
            }
            Join<Hotel, Address> addressJoin = root.join("address", JoinType.LEFT);
            return cb.equal(cb.lower(addressJoin.get("country")), country.toLowerCase().trim());
        };
    }

    public static Specification<Hotel> hasAllAmenities(Set<String> amenities) {
        return (root, query, cb) -> {
            if (amenities == null || amenities.isEmpty()) {
                return cb.conjunction();
            }

            Subquery<Long> subquery = query.subquery(Long.class);
            Root<Hotel> subRoot = subquery.from(Hotel.class);
            Join<Hotel, Amenity> amenityJoin = subRoot.join("amenities");

            subquery.select(cb.countDistinct(amenityJoin.get("id")));
            subquery.where(
                    cb.equal(subRoot.get("id"), root.get("id")),
                    amenityJoin.get("name").in(amenities)
            );

            return cb.equal(subquery, (long) amenities.size());
        };
    }
}
