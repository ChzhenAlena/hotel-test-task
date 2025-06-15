package com.example.hotel.service.strategy;

import java.util.Map;

public interface HistogramStrategy {
    boolean supports(String param);

    Map<String, Long> getHistogram();
}