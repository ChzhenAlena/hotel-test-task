package com.example.hotel.controller;

import com.example.hotel.entity.Address;
import com.example.hotel.entity.Amenity;
import com.example.hotel.entity.ArrivalTime;
import com.example.hotel.entity.Contacts;
import com.example.hotel.entity.Hotel;
import com.example.hotel.repository.AmenityRepository;
import com.example.hotel.repository.HotelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class HistogramControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private AmenityRepository amenityRepository;

    @BeforeEach
    void setUp() {
        hotelRepository.deleteAll();
        amenityRepository.deleteAll();

        Amenity wifi = amenityRepository.save(
                Amenity.builder()
                        .name("Free WiFi")
                        .build());

        Amenity fitness = amenityRepository.save(
                Amenity.builder()
                        .name("Fitness center")
                        .build());

        Amenity business = amenityRepository.save(
                Amenity.builder()
                        .name("Business center")
                        .build());

        amenityRepository.saveAll(List.of(wifi, fitness, business));

        Hotel h1 = Hotel.builder()
                .name("DoubleTree by Hilton Minsk")
                .brand("Hilton")
                .build();

        Address address1 = Address.builder()
                .houseNumber("9")
                .street("Pobediteley Avenue")
                .city("Minsk")
                .country("Belarus")
                .postCode("220004")
                .hotel(h1)
                .build();

        Contacts contacts1 = Contacts.builder()
                .phone("+375 17 309-80-00")
                .email("doubletreeminsk.info@hilton.com")
                .hotel(h1)
                .build();

        ArrivalTime arrivalTime1 = ArrivalTime.builder()
                .checkIn("14:00")
                .checkOut("12:00")
                .hotel(h1)
                .build();

        h1.setAddress(address1);
        h1.setContacts(contacts1);
        h1.setArrivalTime(arrivalTime1);
        h1.setAmenities(Set.of(wifi, fitness));

        Hotel h2 = Hotel.builder()
                .name("Marriott Moscow")
                .brand("Marriott")
                .build();

        Address address2 = Address.builder()
                .houseNumber("1a")
                .street("Tverskaya")
                .city("Moscow")
                .country("Russia")
                .postCode("123456")
                .hotel(h2)
                .build();

        Contacts contacts2 = Contacts.builder()
                .phone("+7 495 123-45-67")
                .email("moscow@marriott.com")
                .hotel(h2)
                .build();

        ArrivalTime arrivalTime2 = ArrivalTime.builder()
                .checkIn("15:00")
                .checkOut("12:00")
                .hotel(h2)
                .build();

        h2.setAddress(address2);
        h2.setContacts(contacts2);
        h2.setArrivalTime(arrivalTime2);
        h2.setAmenities(Set.of(wifi, business));

        hotelRepository.saveAll(List.of(h1, h2));
    }

    @Test
    void getBrandHistogram() throws Exception {
        mockMvc.perform(get("/histogram/brand"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Hilton").value(1))
                .andExpect(jsonPath("$.Marriott").value(1));
    }

    @Test
    void getCityHistogram() throws Exception {
        mockMvc.perform(get("/histogram/city"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Minsk").value(1))
                .andExpect(jsonPath("$.Moscow").value(1));
    }

    @Test
    void getAmenitiesHistogram() throws Exception {
        mockMvc.perform(get("/histogram/amenities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['Free WiFi']").value(2))
                .andExpect(jsonPath("$.['Fitness center']").value(1))
                .andExpect(jsonPath("$.['Business center']").value(1));
    }
}