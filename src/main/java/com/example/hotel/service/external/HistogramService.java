package com.example.hotel.service.external;

import java.util.Map;

public interface HistogramService {
    Map<String, Long> getHistogram(String param);
}
