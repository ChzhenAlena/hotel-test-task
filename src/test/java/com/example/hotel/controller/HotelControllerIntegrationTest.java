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
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class HotelControllerIntegrationTest {

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

        h1.setAmenities(new HashSet<>(Set.of(wifi, spa)));
        hotelDataAccess.save(h1);
    }

    @Test
    void getAllHotels_returnsHotelsList() throws Exception {
        mockMvc.perform(get("/hotels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Hilton Resort"))
                .andExpect(jsonPath("$[0].address").value("10 Ocean Drive, Miami, 33139, USA"))
                .andExpect(jsonPath("$[0].phone").value("+1 305 555 1234"));
    }

    @Test
    void getHotelById_returnsHotelDetails() throws Exception {
        Hotel partialHotel = hotelDataAccess.findAll().getFirst();
        Hotel hotel = hotelDataAccess.findById(partialHotel.getId()).orElseThrow();

        mockMvc.perform(get("/hotels/id")
                        .param("id", hotel.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(hotel.getName()))
                .andExpect(jsonPath("$.brand").value(hotel.getBrand()))
                // Address verification
                .andExpect(jsonPath("$.address.houseNumber").value("10"))
                .andExpect(jsonPath("$.address.street").value("Ocean Drive"))
                .andExpect(jsonPath("$.address.city").value("Miami"))
                .andExpect(jsonPath("$.address.country").value("USA"))
                .andExpect(jsonPath("$.address.postCode").value("33139"))
                // Contacts verification
                .andExpect(jsonPath("$.contacts.phone").value("+1 305 555 1234"))
                .andExpect(jsonPath("$.contacts.email").value("hilton@resort.com"))
                // Arrival time verification
                .andExpect(jsonPath("$.arrivalTime.checkIn").value("14:00"))
                .andExpect(jsonPath("$.arrivalTime.checkOut").value("11:00"))
                // Amenities verification
                .andExpect(jsonPath("$.amenities.length()").value(2))
                .andExpect(jsonPath("$.amenities", hasItem("Free WiFi")))
                .andExpect(jsonPath("$.amenities", hasItem("Spa")))
                .andExpect(jsonPath("$.amenities", not(hasItem("Non-smoking rooms")))); // Negative case
    }

    @Test
    void createHotel_createsNewHotel() throws Exception {
        String newHotelJson = """
                {
                  "name": "Marriott Downtown",
                  "brand": "Marriott",
                  "address": {
                    "houseNumber": "55",
                    "street": "Main Street",
                    "city": "New York",
                    "country": "USA",
                    "postCode": "10001"
                  },
                  "contacts": {
                    "phone": "+1 212 555 5678",
                    "email": "marriott@city.com"
                  },
                  "arrivalTime": {
                    "checkIn": "15:00",
                    "checkOut": "12:00"
                  }
                }
                """;

        mockMvc.perform(post("/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newHotelJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Marriott Downtown"))
                .andExpect(jsonPath("$.address").value("55 Main Street, New York, 10001, USA"))
                .andExpect(jsonPath("$.phone").value("+1 212 555 5678"));

        List<Hotel> hotels = hotelDataAccess.findAll();
        assertEquals(2, hotels.size());
    }

    @Test
    void addAmenitiesToHotel_addsAmenities() throws Exception {
        Hotel hotel = hotelDataAccess.findAll().getFirst();

        String amenitiesJson = """
                ["Pool","Gym"]
                """;

        mockMvc.perform(post("/hotels/{id}/amenities", hotel.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(amenitiesJson))
                .andExpect(status().isOk());

        Hotel updatedHotel = hotelDataAccess.findById(hotel.getId()).orElseThrow();
        assertTrue(updatedHotel.getAmenities().stream()
                .anyMatch(a -> a.getName().equals("Pool")));
        assertTrue(updatedHotel.getAmenities().stream()
                .anyMatch(a -> a.getName().equals("Gym")));
    }
}
