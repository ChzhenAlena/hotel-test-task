package com.example.hotel.service.impl;

import com.example.hotel.domain.model.Address;
import com.example.hotel.domain.model.Amenity;
import com.example.hotel.domain.model.ArrivalTime;
import com.example.hotel.domain.model.Contacts;
import com.example.hotel.domain.model.Hotel;
import com.example.hotel.domain.repository.HotelDataAccess;
import com.example.hotel.dto.HotelCreateRequest;
import com.example.hotel.dto.HotelDetailsDto;
import com.example.hotel.dto.HotelShortDto;
import com.example.hotel.exception.AppException;
import com.example.hotel.exception.ExceptionCode;
import com.example.hotel.mapper.HotelMapper;
import com.example.hotel.service.AmenityService;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HotelServiceImplTest {

    @Mock
    private HotelDataAccess hotelDataAccess;

    @Mock
    private AmenityService amenityService;

    @Mock
    private HotelMapper hotelMapper;

    @InjectMocks
    private HotelServiceImpl hotelService;

    @Test
    void getAll_shouldReturnListOfDtos() {
        Hotel projection = mock(Hotel.class);
        List<Hotel> projections = List.of(projection);
        HotelShortDto dto = Instancio.create(HotelShortDto.class);

        when(hotelDataAccess.findAll()).thenReturn(projections);
        when(hotelMapper.toShortDto(projection)).thenReturn(dto);

        List<HotelShortDto> result = hotelService.getAll();

        assertEquals(1, result.size());
        assertSame(dto, result.getFirst());

        verify(hotelDataAccess).findAll();
        verify(hotelMapper).toShortDto(projection);
    }

    @Test
    void getById_shouldReturnDetailsDto() {
        Long id = 1L;
        Hotel hotel = new Hotel();
        HotelDetailsDto dto = Instancio.create(HotelDetailsDto.class);

        when(hotelDataAccess.findById(id)).thenReturn(Optional.of(hotel));
        when(hotelMapper.toDetailsDto(hotel)).thenReturn(dto);

        HotelDetailsDto result = hotelService.getById(id);

        assertSame(dto, result);
        verify(hotelDataAccess).findById(id);
        verify(hotelMapper).toDetailsDto(hotel);
    }

    @Test
    void getById_shouldThrow_whenNotFound() {
        Long id = 1L;
        when(hotelDataAccess.findById(id)).thenReturn(Optional.empty());

        AppException ex = assertThrows(AppException.class, () -> hotelService.getById(id));

        assertEquals(ExceptionCode.HOTEL_NOT_FOUND_BY_ID, ex.getExceptionCode());
        verify(hotelDataAccess).findById(id);
    }

    @Test
    void create_shouldSaveAndReturnDto() {
        HotelCreateRequest request = Instancio.create(HotelCreateRequest.class);
        Hotel hotel = Instancio.create(Hotel.class);
        Hotel saved = Instancio.create(Hotel.class);
        HotelShortDto dto = Instancio.create(HotelShortDto.class);

        if (hotel.getAddress() == null) {
            hotel.setAddress(new Address());
        }
        if (hotel.getContacts() == null) {
            hotel.setContacts(new Contacts());
        }
        if (hotel.getArrivalTime() == null) {
            hotel.setArrivalTime(new ArrivalTime());
        }

        when(hotelMapper.toDomain(request)).thenReturn(hotel);
        when(hotelDataAccess.save(hotel)).thenReturn(saved);
        when(hotelMapper.toShortDto(saved)).thenReturn(dto);

        HotelShortDto result = hotelService.create(request);

        assertSame(dto, result);
        verify(hotelMapper).toDomain(request);
        verify(hotelDataAccess).save(hotel);
        verify(hotelMapper).toShortDto(saved);
    }

    @Test
    void addAmenities_shouldAddAmenitiesAndSave() {
        Long id = 1L;
        List<String> names = List.of("WiFi", "Pool");

        Hotel hotel = new Hotel();
        hotel.setAmenities(new HashSet<>());

        Amenity amenity1 = Amenity.builder().name("WiFi").build();
        Amenity amenity2 = Amenity.builder().name("Pool").build();
        Set<Amenity> newAmenities = Set.of(amenity1, amenity2);

        when(hotelDataAccess.findById(id)).thenReturn(Optional.of(hotel));
        when(amenityService.getOrCreateAmenities(names)).thenReturn(newAmenities);

        hotelService.addAmenities(id, names);

        assertEquals(2, hotel.getAmenities().size());
        assertTrue(hotel.getAmenities().containsAll(newAmenities));
        verify(hotelDataAccess).findById(id);
        verify(amenityService).getOrCreateAmenities(names);
        verify(hotelDataAccess).save(hotel);
    }

    @Test
    void addAmenities_shouldThrow_whenHotelNotFound() {
        Long id = 99L;
        List<String> names = List.of("WiFi");

        when(hotelDataAccess.findById(id)).thenReturn(Optional.empty());

        AppException ex = assertThrows(AppException.class, () -> hotelService.addAmenities(id, names));

        assertEquals(ExceptionCode.HOTEL_NOT_FOUND_BY_ID, ex.getExceptionCode());
        verify(hotelDataAccess).findById(id);
    }
}