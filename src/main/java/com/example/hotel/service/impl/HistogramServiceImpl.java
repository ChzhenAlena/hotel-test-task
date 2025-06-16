package com.example.hotel.service.impl;

import com.example.hotel.exception.AppException;
import com.example.hotel.exception.ExceptionCode;
import com.example.hotel.service.HistogramService;
import com.example.hotel.service.strategy.HistogramStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class HistogramServiceImpl implements HistogramService {

    private final List<HistogramStrategy> strategies;

    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> getHistogram(String param) {
        return strategies.stream()
                .filter(strategy -> strategy.supports(param))
                .findFirst()
                .orElseThrow(
                        () -> new AppException(ExceptionCode.UNKNOWN_HISTOGRAM_PARAM, HttpStatus.BAD_REQUEST, param))
                .getHistogram();
    }
}
