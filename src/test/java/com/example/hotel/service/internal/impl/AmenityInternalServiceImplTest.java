package com.example.hotel.service.internal.impl;

import com.example.hotel.entity.Amenity;
import com.example.hotel.repository.AmenityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AmenityInternalServiceImplTest {

    @Mock
    private AmenityRepository amenityRepository;

    @InjectMocks
    private AmenityInternalServiceImpl amenityInternalService;

    @Test
    void getOrCreateAmenities_allExisting_shouldReturnExistingAmenities() {
        List<String> names = List.of("WiFi", "Pool");
        Amenity wifi = Amenity.builder().name("WiFi").build();
        Amenity pool = Amenity.builder().name("Pool").build();
        List<Amenity> existing = List.of(wifi, pool);

        when(amenityRepository.findByNameInIgnoreCase(List.of("WiFi", "Pool")))
                .thenReturn(existing);

        Set<Amenity> result = amenityInternalService.getOrCreateAmenities(names);

        assertEquals(Set.of(wifi, pool), result);
        verify(amenityRepository).findByNameInIgnoreCase(List.of("WiFi", "Pool"));
        verify(amenityRepository, never()).saveAll(any());
    }

    @Test
    void getOrCreateAmenities_someMissing_shouldSaveAndReturnAllAmenities() {
        List<String> names = List.of("WiFi", "Gym");
        Amenity wifi = Amenity.builder().name("WiFi").build();
        Amenity gym = Amenity.builder().name("Gym").build();

        when(amenityRepository.findByNameInIgnoreCase(List.of("WiFi", "Gym")))
                .thenReturn(List.of(wifi));

        when(amenityRepository.saveAll(argThat(iterable -> {
            List<Amenity> toCreate = StreamSupport.stream(iterable.spliterator(), false).toList();
            return toCreate.size() == 1 &&
                    toCreate.getFirst().getName().equals("Gym");
        }))).thenReturn(List.of(gym));

        Set<Amenity> result = amenityInternalService.getOrCreateAmenities(names);

        assertEquals(Set.of(wifi, gym), result);
        verify(amenityRepository).findByNameInIgnoreCase(List.of("WiFi", "Gym"));
        verify(amenityRepository).saveAll(argThat(iterable -> {
            List<Amenity> toCreate = StreamSupport.stream(iterable.spliterator(), false).toList();
            return toCreate.size() == 1 &&
                    toCreate.getFirst().getName().equals("Gym");
        }));
    }

    @Test
    void getOrCreateAmenities_allMissing_shouldSaveAndReturnNewAmenities() {
        List<String> names = List.of("Sauna", "Spa");
        Amenity sauna = Amenity.builder().name("Sauna").build();
        Amenity spa = Amenity.builder().name("Spa").build();

        when(amenityRepository.findByNameInIgnoreCase(List.of("Sauna", "Spa")))
                .thenReturn(List.of());

        when(amenityRepository.saveAll(argThat(iterable -> {
            Set<String> namesSet = StreamSupport.stream(iterable.spliterator(), false)
                    .map(Amenity::getName)
                    .collect(Collectors.toSet());
            return namesSet.equals(Set.of("Sauna", "Spa"));
        }))).thenReturn(List.of(sauna, spa));

        Set<Amenity> result = amenityInternalService.getOrCreateAmenities(names);

        assertEquals(Set.of(sauna, spa), result);

        verify(amenityRepository).findByNameInIgnoreCase(List.of("Sauna", "Spa"));
        verify(amenityRepository).saveAll(any());
    }
}
