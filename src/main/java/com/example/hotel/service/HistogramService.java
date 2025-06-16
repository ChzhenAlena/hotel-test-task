package com.example.hotel.service;

import java.util.Map;

public interface HistogramService {
    Map<String, Long> getHistogram(String param);
}
