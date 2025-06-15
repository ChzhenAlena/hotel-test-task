package com.example.hotel.service.internal.impl;

import com.example.hotel.entity.Hotel;
import com.example.hotel.repository.HotelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class HotelSearchInternalServiceImplTest {

    private HotelRepository hotelRepository;
    private HotelSearchInternalServiceImpl hotelSearchInternalService;

    @BeforeEach
    void setUp() {
        hotelRepository = mock(HotelRepository.class);
        hotelSearchInternalService = new HotelSearchInternalServiceImpl(hotelRepository);
    }

    @Test
    void search_CallsRepositoryWithSpecification() {
        String name = "HotelName";
        String brand = "BrandX";
        String city = "CityY";
        String country = "CountryZ";
        Set<String> amenities = Set.of("Pool", "Gym");

        List<Hotel> expectedHotels = List.of(mock(Hotel.class), mock(Hotel.class));
        when(hotelRepository.findAll((Specification<Hotel>) any())).thenReturn(expectedHotels);

        List<Hotel> result = hotelSearchInternalService.search(name, brand, city, country, amenities);

        assertEquals(expectedHotels, result);
        ArgumentCaptor<Specification<Hotel>> specCaptor = (ArgumentCaptor<Specification<Hotel>>)
                (ArgumentCaptor<?>) ArgumentCaptor.forClass(Specification.class);
        verify(hotelRepository).findAll(specCaptor.capture());

        Specification<Hotel> usedSpec = specCaptor.getValue();
        assertNotNull(usedSpec);
    }

    @Test
    void search_AllNullParameters_ReturnsAllHotels() {
        when(hotelRepository.findAll((Specification<Hotel>) any())).thenReturn(List.of());

        List<Hotel> result = hotelSearchInternalService.search(null, null, null, null, null);

        assertNotNull(result);
        verify(hotelRepository).findAll((Specification<Hotel>) any());
    }
}