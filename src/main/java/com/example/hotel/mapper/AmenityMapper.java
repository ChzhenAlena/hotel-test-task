package com.example.hotel.mapper;

import com.example.hotel.domain.model.Amenity;
import com.example.hotel.infrastructure.jpa.entity.AmenityEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AmenityMapper {

    AmenityEntity toEntity(Amenity entity);

    Amenity toDomain(AmenityEntity entity);
}

