package com.example.hotel.service.external.impl;

import com.example.hotel.service.internal.HistogramInternalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class HistogramServiceImplTest {

    private HistogramInternalService internalService;
    private HistogramServiceImpl histogramService;

    @BeforeEach
    void setUp() {
        internalService = mock(HistogramInternalService.class);
        histogramService = new HistogramServiceImpl(internalService);
    }

    @Test
    void getHistogram_DelegatesToInternalService() {
        String param = "brand";
        Map<String, Long> expected = Map.of("Hilton", 10L);

        when(internalService.getHistogramByParam(param)).thenReturn(expected);

        Map<String, Long> result = histogramService.getHistogram(param);

        assertEquals(expected, result);
        verify(internalService).getHistogramByParam(param);
    }
}