package com.example.hotel.mapper;

import com.example.hotel.domain.model.AddressLike;
import com.example.hotel.domain.model.Amenity;
import com.example.hotel.domain.model.Hotel;
import com.example.hotel.domain.projection.HotelProjection;
import com.example.hotel.dto.HotelCreateRequest;
import com.example.hotel.dto.HotelDetailsDto;
import com.example.hotel.dto.HotelShortDto;
import com.example.hotel.infrastructure.jpa.entity.HotelEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface HotelMapper {

    @Named("formatAddress")
    static String formatAddress(AddressLike a) {
        if (a == null) {
            return null;
        }
        return String.format(
                "%s %s, %s, %s, %s",
                a.getHouseNumber(),
                a.getStreet(),
                a.getCity(),
                a.getPostCode(),
                a.getCountry()
        );
    }

    @Named("mapAmenities")
    static Set<String> mapAmenities(Set<Amenity> amenities) {
        if (amenities == null) {
            return Set.of();
        }
        return amenities.stream()
                .map(Amenity::getName)
                .collect(Collectors.toSet());
    }

    @Mapping(target = "address.houseNumber", source = "houseNumber")
    @Mapping(target = "address.street", source = "street")
    @Mapping(target = "address.city", source = "city")
    @Mapping(target = "address.postCode", source = "postCode")
    @Mapping(target = "address.country", source = "country")
    @Mapping(target = "contacts.phone", source = "phone")
    Hotel toDomain(HotelProjection entity);

    @Mapping(target = "address", source = "address", qualifiedByName = "formatAddress")
    @Mapping(target = "phone", source = "contacts.phone")
    HotelShortDto toShortDto(Hotel entity);

    @Mapping(target = "amenities", source = "amenities", qualifiedByName = "mapAmenities")
    HotelDetailsDto toDetailsDto(Hotel entity);

    @Mapping(target = "address.hotel", ignore = true)
    @Mapping(target = "contacts.hotel", ignore = true)
    @Mapping(target = "arrivalTime.hotel", ignore = true)
    HotelEntity toEntity(Hotel domain);

    Hotel toDomain(HotelCreateRequest createRequest);

    @Mapping(target = "address.hotel", ignore = true)
    @Mapping(target = "contacts.hotel", ignore = true)
    @Mapping(target = "arrivalTime.hotel", ignore = true)
    Hotel toDomain(HotelEntity entity);

    @AfterMapping
    default void setBackReferences(@MappingTarget HotelEntity hotelEntity) {
        if (hotelEntity.getAddress() != null) {
            hotelEntity.getAddress().setHotel(hotelEntity);
        }
        if (hotelEntity.getContacts() != null) {
            hotelEntity.getContacts().setHotel(hotelEntity);
        }
        if (hotelEntity.getArrivalTime() != null) {
            hotelEntity.getArrivalTime().setHotel(hotelEntity);
        }
    }
}

