package com.example.hotel;

import org.springframework.boot.SpringApplication;

public class TestHotelApplication {

    public static void main(String[] args) {
        SpringApplication.from(HotelApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
