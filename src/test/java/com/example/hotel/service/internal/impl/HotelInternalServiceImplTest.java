package com.example.hotel.service.internal.impl;

import com.example.hotel.entity.Hotel;
import com.example.hotel.entity.projection.HotelProjection;
import com.example.hotel.exception.AppException;
import com.example.hotel.exception.ExceptionCode;
import com.example.hotel.repository.HotelRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HotelInternalServiceImplTest {

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private HotelInternalServiceImpl hotelInternalService;

    @Test
    void findAll_shouldReturnAllHotels() {
        HotelProjection hotelProjection1 = mock(HotelProjection.class);
        HotelProjection hotelProjection2 = mock(HotelProjection.class);
        List<HotelProjection> projections = List.of(hotelProjection1, hotelProjection2);

        when(hotelRepository.findAllHotelProjections()).thenReturn(projections);

        List<HotelProjection> result = hotelInternalService.findAll();

        assertEquals(projections, result);
        verify(hotelRepository).findAllHotelProjections();
    }

    @Test
    void findById_existingId_shouldReturnHotel() {
        Long id = 1L;
        Hotel hotel = new Hotel();
        when(hotelRepository.findById(id)).thenReturn(Optional.of(hotel));

        Hotel result = hotelInternalService.findById(id);

        assertEquals(hotel, result);
        verify(hotelRepository).findById(id);
    }

    @Test
    void findById_nonExistingId_shouldThrowServiceException() {
        Long id = 1L;
        when(hotelRepository.findById(id)).thenReturn(Optional.empty());

        AppException ex = assertThrows(AppException.class, () -> hotelInternalService.findById(id));

        assertEquals(ExceptionCode.HOTEL_NOT_FOUND_BY_ID, ex.getExceptionCode());
        assertEquals(HttpStatus.NOT_FOUND, ex.getHttpStatus());
        verify(hotelRepository).findById(id);
    }

    @Test
    void save_shouldReturnSavedHotel() {
        Hotel hotel = new Hotel();
        when(hotelRepository.save(hotel)).thenReturn(hotel);

        Hotel result = hotelInternalService.save(hotel);

        assertEquals(hotel, result);
        verify(hotelRepository).save(hotel);
    }
}
