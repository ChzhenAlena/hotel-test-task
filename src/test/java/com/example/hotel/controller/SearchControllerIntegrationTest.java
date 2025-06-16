package com.example.hotel.controller;

import com.example.hotel.domain.model.Address;
import com.example.hotel.domain.model.Amenity;
import com.example.hotel.domain.model.ArrivalTime;
import com.example.hotel.domain.model.Contacts;
import com.example.hotel.domain.model.Hotel;
import com.example.hotel.domain.repository.AmenityDataAccess;
import com.example.hotel.domain.repository.HotelDataAccess;
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
class SearchControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private HotelDataAccess hotelDataAccess;

    @Autowired
    private AmenityDataAccess amenityDataAccess;

    @BeforeEach
    void setUp() {
        hotelDataAccess.deleteAll();
        amenityDataAccess.deleteAll();

        Amenity wifi = amenityDataAccess.save(Amenity.builder().name("Free WiFi").build());
        Amenity spa = amenityDataAccess.save(Amenity.builder().name("Spa").build());

        Hotel h1 = Hotel.builder()
                .name("Hilton Resort")
                .brand("Hilton")
                .build();

        h1.setAddress(Address.builder()
                .houseNumber("10")
                .street("Ocean Drive")
                .city("Miami")
                .country("USA")
                .postCode("33139")
                .hotel(h1)
                .build());

        h1.setContacts(Contacts.builder()
                .phone("+1 305 555 1234")
                .email("hilton@resort.com")
                .hotel(h1)
                .build());

        h1.setArrivalTime(ArrivalTime.builder()
                .checkIn("14:00")
                .checkOut("11:00")
                .hotel(h1)
                .build());

        h1.setAmenities(Set.of(wifi, spa));

        Hotel h2 = Hotel.builder()
                .name("City Marriott")
                .brand("Marriott")
                .build();

        h2.setAddress(Address.builder()
                .houseNumber("55")
                .street("Main Street")
                .city("New York")
                .country("USA")
                .postCode("10001")
                .hotel(h2)
                .build());

        h2.setContacts(Contacts.builder()
                .phone("+1 212 555 5678")
                .email("marriott@city.com")
                .hotel(h2)
                .build());

        h2.setArrivalTime(ArrivalTime.builder()
                .checkIn("15:00")
                .checkOut("12:00")
                .hotel(h2)
                .build());

        h2.setAmenities(Set.of(wifi));

        hotelDataAccess.saveAll(List.of(h1, h2));
    }

    @Test
    void searchByCity() throws Exception {
        mockMvc.perform(get("/search")
                        .param("city", "Miami"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Hilton Resort"));
    }

    @Test
    void searchByBrand() throws Exception {
        mockMvc.perform(get("/search")
                        .param("brand", "Marriott"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("City Marriott"));
    }

    @Test
    void searchByAmenity() throws Exception {
        mockMvc.perform(get("/search")
                        .param("amenities", "Spa"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Hilton Resort"));
    }

    @Test
    void searchByMultipleFilters() throws Exception {
        mockMvc.perform(get("/search")
                        .param("brand", "Hilton")
                        .param("city", "Miami")
                        .param("amenities", "Free WiFi,Spa"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Hilton Resort"));
    }

    @Test
    void searchWithNoFiltersReturnsAll() throws Exception {
        mockMvc.perform(get("/search"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }
}