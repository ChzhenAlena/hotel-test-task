package com.example.hotel.service.internal;

import java.util.Map;

public interface HistogramInternalService {
    Map<String, Long> getHistogramByParam(String param);
}