package com.wolfTech.recommendationService.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.wolfTech.recommendationService.model.CryptoData;
import com.wolfTech.recommendationService.service.CryptoService;

class CryptoControllerTest {

	@Mock
    private CryptoService cryptoService;

    @InjectMocks
    private CryptoController cryptoController;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllCryptoList() {
        List<CryptoData> cryptoDataList = new ArrayList<>();
        cryptoDataList.add(new CryptoData(1637670000000L, "BTC", 1));
        cryptoDataList.add(new CryptoData(1637680000000L, "ETH", 2));
        cryptoDataList.add(new CryptoData(1637690000000L, "XRP", 3));

        when(cryptoService.readAllCryptoData()).thenReturn(cryptoDataList);

        ResponseEntity<List<CryptoData>> responseEntity = cryptoController.getAllCryptoList();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(cryptoDataList, responseEntity.getBody());
    }

    @Test
    public void testGetSpecificCryptoData() {
        List<CryptoData> specificCryptoData = new ArrayList<>();
        specificCryptoData.add(new CryptoData(1637670000000L, "BTC", 1));

        when(cryptoService.readCryptoData("BTC")).thenReturn(specificCryptoData);

        ResponseEntity<List<CryptoData>> responseEntity = cryptoController.getSpecificCryptoData("BTC");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(specificCryptoData, responseEntity.getBody());
    }
    
    @Test
    public void testGetSortNormCryptos() {
        Map<String, Double> sortNormCrypto = new HashMap<>();
        sortNormCrypto.put("BTC", 0.5);
        sortNormCrypto.put("ETH", 0.75);
        sortNormCrypto.put("XRP", 0.25);

        when(cryptoService.getSortedNormalizedCryptos()).thenReturn(sortNormCrypto);

        ResponseEntity<Map<String, Double>> responseEntity = cryptoController.getSortNormCryptos();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(sortNormCrypto, responseEntity.getBody());
    }
    
    @Test
    public void testGetSpecificMinCryptoData() {
        List<CryptoData> specificMinCryptoData = new ArrayList<>();
        specificMinCryptoData.add(new CryptoData(1637670000000L, "BTC", 1));

        when(cryptoService.getMinCryptoPrice(cryptoService.readCryptoData("BTC"))).thenReturn(specificMinCryptoData.get(0));

        ResponseEntity<CryptoData> responseEntity = cryptoController.getSpecificMinCryptoData("BTC");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(specificMinCryptoData.get(0), responseEntity.getBody());
    }
    
    @Test
    public void testGetSpecificMaxCryptoData() {
        List<CryptoData> specificMaxCryptoData = new ArrayList<>();
        specificMaxCryptoData.add(new CryptoData(1637690000000L, "XRP", 3));

        when(cryptoService.getMaxCryptoPrice(cryptoService.readCryptoData("XRP"))).thenReturn(specificMaxCryptoData.get(0));

        ResponseEntity<CryptoData> responseEntity = cryptoController.getSpecificMaxCryptoData("XRP");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(specificMaxCryptoData.get(0), responseEntity.getBody());
    }
    
    @Test
    public void testGetHighestCryptoForDay() {
        String date = "2022-01-01";

        Map.Entry<String, Double> highestCrypto = Map.entry("BTC", 0.75);

        when(cryptoService.getHighestNormCryptoForDay(date)).thenReturn(highestCrypto);

        ResponseEntity<Map.Entry<String, Double>> responseEntity = cryptoController.getHighestCryptoForDay(date);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(highestCrypto, responseEntity.getBody());
    }
    
    @Test
    public void testGetHighestCryptoForDateRange() {
        String startDate = "2022-01-01";
        String endDate = "2022-01-03";

        Map.Entry<String, Double> highestCrypto = Map.entry("BTC", 0.75);

        when(cryptoService.getHighestNormCryptoForDateRange(startDate, endDate)).thenReturn(highestCrypto);

        ResponseEntity<Map.Entry<String, Double>> responseEntity = cryptoController.getHighestCryptoForDay(startDate, endDate);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(highestCrypto, responseEntity.getBody());
    }

}
