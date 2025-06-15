package com.example.hotel.service.external.impl;

import com.example.hotel.service.external.HistogramService;
import com.example.hotel.service.internal.HistogramInternalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class HistogramServiceImpl implements HistogramService {

    private final HistogramInternalService internalService;

    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> getHistogram(String param) {
        return internalService.getHistogramByParam(param);
    }
}
