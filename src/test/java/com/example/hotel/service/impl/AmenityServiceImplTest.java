package com.example.hotel.service.impl;

import com.example.hotel.domain.model.Amenity;
import com.example.hotel.domain.repository.AmenityDataAccess;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AmenityServiceImplTest {

    @Mock
    private AmenityDataAccess dataAccess;

    @InjectMocks
    private AmenityServiceImpl amenityInternalService;

    @Test
    void getOrCreateAmenities_allExisting_shouldReturnExistingAmenities() {
        List<String> names = List.of("WiFi", "Pool");
        Amenity wifi = Amenity.builder().name("WiFi").build();
        Amenity pool = Amenity.builder().name("Pool").build();
        List<Amenity> existing = List.of(wifi, pool);

        when(dataAccess.findByNameInIgnoreCase(List.of("WiFi", "Pool")))
                .thenReturn(existing);

        Set<Amenity> result = amenityInternalService.getOrCreateAmenities(names);

        assertEquals(Set.of(wifi, pool), result);
        verify(dataAccess).findByNameInIgnoreCase(List.of("WiFi", "Pool"));
        verify(dataAccess, never()).saveAll(any());
    }

    @Test
    void getOrCreateAmenities_someMissing_shouldSaveAndReturnAllAmenities() {
        List<String> names = List.of("WiFi", "Gym");
        Amenity wifi = Amenity.builder().name("WiFi").build();
        Amenity gym = Amenity.builder().name("Gym").build();

        when(dataAccess.findByNameInIgnoreCase(List.of("WiFi", "Gym")))
                .thenReturn(List.of(wifi));

        when(dataAccess.saveAll(argThat(iterable -> {
            List<Amenity> toCreate = iterable.stream().toList();
            return toCreate.size() == 1 &&
                    toCreate.getFirst().getName().equals("Gym");
        }))).thenReturn(List.of(gym));

        Set<Amenity> result = amenityInternalService.getOrCreateAmenities(names);

        assertEquals(Set.of(wifi, gym), result);
        verify(dataAccess).findByNameInIgnoreCase(List.of("WiFi", "Gym"));
        verify(dataAccess).saveAll(argThat(iterable -> {
            List<Amenity> toCreate = iterable.stream().toList();
            return toCreate.size() == 1 &&
                    toCreate.getFirst().getName().equals("Gym");
        }));
    }

    @Test
    void getOrCreateAmenities_allMissing_shouldSaveAndReturnNewAmenities() {
        List<String> names = List.of("Sauna", "Spa");
        Amenity sauna = Amenity.builder().name("Sauna").build();
        Amenity spa = Amenity.builder().name("Spa").build();

        when(dataAccess.findByNameInIgnoreCase(List.of("Sauna", "Spa")))
                .thenReturn(List.of());

        when(dataAccess.saveAll(argThat(iterable -> {
            Set<String> namesSet = iterable.stream()
                    .map(Amenity::getName)
                    .collect(Collectors.toSet());
            return namesSet.equals(Set.of("Sauna", "Spa"));
        }))).thenReturn(List.of(sauna, spa));

        Set<Amenity> result = amenityInternalService.getOrCreateAmenities(names);

        assertEquals(Set.of(sauna, spa), result);

        verify(dataAccess).findByNameInIgnoreCase(List.of("Sauna", "Spa"));
        verify(dataAccess).saveAll(any());
    }
}
