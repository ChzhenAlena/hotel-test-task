package com.example.hotel.service.external.impl;

import com.example.hotel.dto.HotelCreateRequest;
import com.example.hotel.dto.HotelDetailsDto;
import com.example.hotel.dto.HotelShortDto;
import com.example.hotel.entity.Address;
import com.example.hotel.entity.Amenity;
import com.example.hotel.entity.ArrivalTime;
import com.example.hotel.entity.Contacts;
import com.example.hotel.entity.Hotel;
import com.example.hotel.entity.projection.HotelProjection;
import com.example.hotel.mapper.HotelMapper;
import com.example.hotel.service.internal.AmenityInternalService;
import com.example.hotel.service.internal.HotelInternalService;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HotelServiceImplTest {

    @Mock
    private HotelInternalService hotelInternalServiceImpl;

    @Mock
    private AmenityInternalService amenityInternalServiceImpl;

    @Mock
    private HotelMapper hotelMapper;

    @InjectMocks
    private HotelServiceImpl hotelService;

    @Test
    void getAll_shouldReturnListOfDtos() {
        HotelProjection hotelProjection = mock(HotelProjection.class);
        List<HotelProjection> projections = List.of(hotelProjection);

        HotelShortDto dto = new HotelShortDto(1L, "Name", "Desc", "Address", "Phone");

        when(hotelInternalServiceImpl.findAll()).thenReturn(projections);
        when(hotelMapper.toShortDto(hotelProjection)).thenReturn(dto);

        List<HotelShortDto> result = hotelService.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertSame(dto, result.getFirst());

        verify(hotelInternalServiceImpl).findAll();
        verify(hotelMapper).toShortDto(hotelProjection);
    }

    @Test
    void getById_shouldReturnDetailsDto() {
        Long id = 1L;
        Hotel hotel = new Hotel();
        HotelDetailsDto detailsDto = Instancio.create(HotelDetailsDto.class);

        when(hotelInternalServiceImpl.findById(id)).thenReturn(hotel);
        when(hotelMapper.toDetailsDto(hotel)).thenReturn(detailsDto);

        HotelDetailsDto result = hotelService.getById(id);

        assertNotNull(result);
        assertSame(detailsDto, result);

        verify(hotelInternalServiceImpl).findById(id);
        verify(hotelMapper).toDetailsDto(hotel);
    }

    @Test
    void create_shouldSaveAndReturnDto() {
        HotelCreateRequest request = Instancio.create(HotelCreateRequest.class);

        Hotel entity = Instancio.create(Hotel.class);
        if (entity.getAddress() == null) entity.setAddress(new Address());
        if (entity.getContacts() == null) entity.setContacts(new Contacts());
        if (entity.getArrivalTime() == null) entity.setArrivalTime(new ArrivalTime());

        Hotel savedEntity = Instancio.create(Hotel.class);
        HotelShortDto dto = new HotelShortDto(1L, "Name", "Desc", "Address", "Phone");

        when(hotelMapper.toEntity(request)).thenReturn(entity);
        when(hotelInternalServiceImpl.save(entity)).thenReturn(savedEntity);
        when(hotelMapper.toShortDto(savedEntity)).thenReturn(dto);

        HotelShortDto result = hotelService.create(request);

        assertNotNull(result);
        assertSame(dto, result);

        verify(hotelMapper).toEntity(request);
        verify(hotelInternalServiceImpl).save(entity);
        verify(hotelMapper).toShortDto(savedEntity);
    }

    @Test
    void addAmenities_shouldAddAndSaveAmenities() {
        Long id = 1L;
        List<String> amenityNames = List.of("WiFi", "Pool");
        Hotel hotel = new Hotel();
        hotel.setAmenities(new HashSet<>());

        Amenity amenity1 = new Amenity();
        amenity1.setName("WiFi");

        Amenity amenity2 = new Amenity();
        amenity2.setName("Pool");

        Set<Amenity> amenities = Set.of(amenity1, amenity2);

        when(hotelInternalServiceImpl.findById(id)).thenReturn(hotel);
        when(amenityInternalServiceImpl.getOrCreateAmenities(amenityNames)).thenReturn(amenities);

        hotelService.addAmenities(id, amenityNames);

        assertTrue(hotel.getAmenities().containsAll(amenities));
        assertEquals(2, hotel.getAmenities().size());

        verify(hotelInternalServiceImpl).findById(id);
        verify(amenityInternalServiceImpl).getOrCreateAmenities(amenityNames);
        verify(hotelInternalServiceImpl).save(hotel);
    }
}