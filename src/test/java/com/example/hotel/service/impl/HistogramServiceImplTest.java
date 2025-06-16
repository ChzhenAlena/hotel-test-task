package com.example.hotel.service.impl;

import com.example.hotel.exception.AppException;
import com.example.hotel.exception.ExceptionCode;
import com.example.hotel.service.strategy.HistogramStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class HistogramServiceImplTest {

    private HistogramServiceImpl histogramService;

    private HistogramStrategy brandStrategy;
    private HistogramStrategy cityStrategy;

    @BeforeEach
    void setUp() {
        brandStrategy = mock(HistogramStrategy.class);
        cityStrategy = mock(HistogramStrategy.class);

        histogramService = new HistogramServiceImpl(List.of(brandStrategy, cityStrategy));
    }

    @Test
    void getHistogram_ReturnsHistogram_WhenStrategySupportsParam() {
        String param = "brand";
        Map<String, Long> expectedMap = Map.of("Hilton", 15L, "Marriott", 7L);

        when(brandStrategy.supports(param)).thenReturn(true);
        when(brandStrategy.getHistogram()).thenReturn(expectedMap);
        when(cityStrategy.supports(param)).thenReturn(false);

        Map<String, Long> result = histogramService.getHistogram(param);

        assertEquals(expectedMap, result);
        verify(brandStrategy).getHistogram();
        verify(cityStrategy, never()).getHistogram();
    }

    @Test
    void getHistogram_ThrowsException_WhenNoStrategySupportsParam() {
        String param = "unknownParam";

        when(brandStrategy.supports(param)).thenReturn(false);
        when(cityStrategy.supports(param)).thenReturn(false);

        AppException exception = assertThrows(AppException.class, () -> histogramService.getHistogram(param));

        assertEquals(ExceptionCode.UNKNOWN_HISTOGRAM_PARAM, exception.getExceptionCode());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }
}