package com.example.hotel.service.impl;

import com.example.hotel.domain.filter.HotelSearchFilter;
import com.example.hotel.domain.model.Hotel;
import com.example.hotel.domain.repository.HotelSearchDataAccess;
import com.example.hotel.dto.HotelShortDto;
import com.example.hotel.mapper.HotelMapper;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HotelSearchServiceImplTest {

    @Mock
    private HotelSearchDataAccess dataAccess;

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
        when(dataAccess.search(any(HotelSearchFilter.class))).thenReturn(hotels);
        when(hotelMapper.toShortDto(hotelEntity)).thenReturn(dto);

        List<HotelShortDto> result = hotelSearchService.search(name, brand, city, country, amenities);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertSame(dto, result.getFirst());

        ArgumentCaptor<HotelSearchFilter> captor = ArgumentCaptor.forClass(HotelSearchFilter.class);
        verify(dataAccess).search(captor.capture());
        HotelSearchFilter usedFilter = captor.getValue();

        assertEquals(name, usedFilter.name());
        assertEquals(brand, usedFilter.brand());
        assertEquals(city, usedFilter.city());
        assertEquals(country, usedFilter.country());
        assertEquals(amenities, usedFilter.amenities());

        verify(hotelMapper).toShortDto(hotelEntity);
    }

    @Test
    void search_whenEmptyResult_shouldReturnEmptyList() {
        when(dataAccess.search(any())).thenReturn(List.of());

        List<HotelShortDto> result = hotelSearchService.search(null, null, null, null, null);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(dataAccess).search(any());
        verifyNoInteractions(hotelMapper);
    }
}