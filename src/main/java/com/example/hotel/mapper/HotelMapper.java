package com.example.hotel.mapper;

import com.example.hotel.dto.HotelCreateRequest;
import com.example.hotel.dto.HotelDetailsDto;
import com.example.hotel.dto.HotelShortDto;
import com.example.hotel.entity.AddressLike;
import com.example.hotel.entity.Amenity;
import com.example.hotel.entity.Hotel;
import com.example.hotel.entity.projection.HotelProjection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface HotelMapper {

    @Mapping(target = "address", source = ".", qualifiedByName = "formatAddress")
    HotelShortDto toShortDto(HotelProjection entity);

    @Mapping(target = "address", source = "address", qualifiedByName = "formatAddress")
    @Mapping(target = "phone", source = "contacts.phone")
    HotelShortDto toShortDto(Hotel entity);

    @Mapping(target = "amenities", source = "amenities", qualifiedByName = "mapAmenities")
    HotelDetailsDto toDetailsDto(Hotel entity);

    Hotel toEntity(HotelCreateRequest createRequest);

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
}

