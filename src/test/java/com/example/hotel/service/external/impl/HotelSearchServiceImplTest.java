package com.example.hotel.service.external.impl;

import com.example.hotel.dto.HotelShortDto;
import com.example.hotel.entity.Hotel;
import com.example.hotel.mapper.HotelMapper;
import com.example.hotel.service.internal.HotelSearchInternalService;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HotelSearchServiceImplTest {

    @Mock
    private HotelSearchInternalService internalService;

    @Mock
    private HotelMapper hotelMapper;

    @InjectMocks
    private HotelSearchServiceImpl hotelSearchService;

    @Test
    void search_shouldReturnMappedDtoList() {
        String name = "HotelName";
        String brand = "BrandX";
        String city = "CityY";
        String country = "CountryZ";
        Set<String> amenities = Set.of("WiFi", "Pool");

        Hotel hotelEntity = new Hotel();
        HotelShortDto dto = Instancio.create(HotelShortDto.class);

        List<Hotel> hotels = List.of(hotelEntity);

        when(internalService.search(name, brand, city, country, amenities)).thenReturn(hotels);
        when(hotelMapper.toShortDto(hotelEntity)).thenReturn(dto);

        List<HotelShortDto> result = hotelSearchService.search(name, brand, city, country, amenities);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertSame(dto, result.getFirst());

        verify(internalService).search(name, brand, city, country, amenities);
        verify(hotelMapper).toShortDto(hotelEntity);
    }
}