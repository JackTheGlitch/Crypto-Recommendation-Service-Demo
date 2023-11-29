package com.wolfTech.recommendationService.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.wolfTech.recommendationService.model.CryptoData;

class CryptoServiceTest {
	
	private CryptoService cryptoService;

    @BeforeEach
    void setUp() {
        cryptoService = new CryptoService();
    }

    @Test
    void testGetHighestNormCryptoForDay() {
    	CryptoService cryptoServiceMock = mock(CryptoService.class);
        Map<String, Double> mockNormalizedCryptoMap = new HashMap<>();
        mockNormalizedCryptoMap.put("XRP", 0.7);
        mockNormalizedCryptoMap.put("BTC", 0.5);
        mockNormalizedCryptoMap.put("ETH", 0.3);

        when(cryptoServiceMock.getHighestNormCryptoForDay("2023/11/27")).thenReturn(
                mockNormalizedCryptoMap.entrySet().iterator().next());

        Map.Entry<String, Double> highestCryptoForDay = cryptoServiceMock.getHighestNormCryptoForDay("2023/11/27");
        assertEquals("XRP", highestCryptoForDay.getKey());
        assertEquals(0.7, highestCryptoForDay.getValue());
    }

    @Test
    void testGetHighestNormCryptoForDateRange() {

    }

    @Test
    void testReadAllCryptoData() {
        List<Path> mockCryptoFiles = new ArrayList<>();
        mockCryptoFiles.add(Path.of("BTC_values.csv"));
        mockCryptoFiles.add(Path.of("ETH_values.csv"));

        CryptoService cryptoServiceMock = mock(CryptoService.class);
        try {
            DirectoryStream<Path> mockDirectoryStream = mock(DirectoryStream.class);
            when(mockDirectoryStream.iterator()).thenReturn(mockCryptoFiles.iterator());
            when(Files.newDirectoryStream(Path.of("src/main/resources/prices/"), "*.csv")).thenReturn(mockDirectoryStream);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<CryptoData> expectedCryptoData = new ArrayList<>();
        expectedCryptoData.add(new CryptoData(1637670000000L, "BTC", 1));
        expectedCryptoData.add(new CryptoData(1637680000000L, "ETH", 2));

        for (Path mockFile : mockCryptoFiles) {
            when(cryptoServiceMock.readCryptoData(mockFile.getFileName().toString().split("_")[0])).thenReturn(expectedCryptoData);
        }

        List<CryptoData> actualCryptoData = cryptoServiceMock.readAllCryptoData();

        assertEquals(mockCryptoFiles.size() * expectedCryptoData.size(), actualCryptoData.size());
    }

    @Test
    void testGetSortedNormalizedCryptos() {
    	CryptoService cryptoServiceMock = mock(CryptoService.class);

        Map<String, Double> mockNormalizedCryptoMap = new HashMap<>();
        mockNormalizedCryptoMap.put("BTC", 0.5);
        mockNormalizedCryptoMap.put("ETH", 0.3);
        mockNormalizedCryptoMap.put("XRP", 0.7);

        when(cryptoServiceMock.getSortedNormalizedCryptos()).thenReturn(mockNormalizedCryptoMap);

        Map<String, Double> sortedNormalizedCryptoMap = cryptoServiceMock.getSortedNormalizedCryptos();

        int expectedSize = 3;
        assertEquals(expectedSize, sortedNormalizedCryptoMap.size());
    }

    @Test
    void testReadCryptoData() {
        BufferedReader bufferedReaderMock = mock(BufferedReader.class);
        try {
            when(bufferedReaderMock.readLine())
                    .thenReturn("1637670000000,BTC,1", "1637680000000,ETH,2", null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        CryptoService cryptoServiceMock = mock(CryptoService.class);
        try {
            FileReader fileReaderMock = mock(FileReader.class);
            when(fileReaderMock.read()).thenReturn(1);
            when(Files.newBufferedReader(Path.of("src/main/resources/prices/BTC_values.csv"))).thenReturn(bufferedReaderMock);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<CryptoData> expectedCryptoData = new ArrayList<>();
        expectedCryptoData.add(new CryptoData(1637670000000L, "BTC", 1));
        expectedCryptoData.add(new CryptoData(1637680000000L, "ETH", 2));

        when(cryptoServiceMock.readCryptoData("BTC")).thenReturn(expectedCryptoData);

        List<CryptoData> actualCryptoData = cryptoServiceMock.readCryptoData("BTC");

        assertEquals(expectedCryptoData.size(), actualCryptoData.size());
    }

    @Test
    void testGetSortedNormalizedCryptoMap() {

    }

    @Test
    void testGetAvailableCryptos() {
        List<Path> mockCryptoFiles = new ArrayList<>();
        mockCryptoFiles.add(Paths.get("BTC_values.csv"));
        mockCryptoFiles.add(Paths.get("ETH_values.csv"));
        mockCryptoFiles.add(Paths.get("XRP_values.csv"));

        CryptoService cryptoServiceMock = mock(CryptoService.class);
        try {
            DirectoryStream<Path> mockDirectoryStream = mock(DirectoryStream.class);
            when(mockDirectoryStream.iterator()).thenReturn(mockCryptoFiles.iterator());
            when(Files.newDirectoryStream(Paths.get("src/main/resources/prices/"), "*.csv")).thenReturn(mockDirectoryStream);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<String> expectedCryptos = List.of("BTC", "ETH", "XRP");
        when(cryptoServiceMock.getAvailableCryptos()).thenReturn(expectedCryptos);

        List<String> actualCryptos = cryptoServiceMock.getAvailableCryptos();

        assertEquals(expectedCryptos.size(), actualCryptos.size());
        assertEquals(expectedCryptos, actualCryptos);
    }

    @Test
    void testNormalize() {
    	cryptoService = mock(CryptoService.class);

        List<CryptoData> testData = new ArrayList<>();
        testData.add(new CryptoData(1637670000000L, "BTC", 1));
        testData.add(new CryptoData(1637680000000L, "ETH", 2));
        testData.add(new CryptoData(1637690000000L, "XRP", 3));

        when(cryptoService.readCryptoData("BTC")).thenReturn(testData);
        when(cryptoService.getMaxCryptoPrice(testData)).thenReturn(new CryptoData(1637690000000L, "XRP", 3));
        when(cryptoService.getMinCryptoPrice(testData)).thenReturn(new CryptoData(1637670000000L, "BTC", 1));

        double result = cryptoService.normalize("BTC");

        assertEquals(0.0, result);
    }

    @Test
    void testNormalizeWithTimestamps() {
    	CryptoService cryptoServiceMock = mock(CryptoService.class);

        List<CryptoData> mockCryptoDataList = new ArrayList<>();
        mockCryptoDataList.add(new CryptoData(1637670000000L, "BTC", 10.0));
        mockCryptoDataList.add(new CryptoData(1637680000000L, "BTC", 20.0));

        when(cryptoServiceMock.getCryptoDataForDay("BTC", 1637670000000L, 1637680000000L)).thenReturn(mockCryptoDataList);

        double expectedNormalizedValue = (20.0 - 10.0) / 10.0;
        double actualNormalizedValue = cryptoServiceMock.normalize("BTC", 1637670000000L, 1637680000000L);

        assertEquals(expectedNormalizedValue, actualNormalizedValue);
    }

    @Test
    void testGetCryptoDataForDay() {
        CryptoData crypto1 = new CryptoData(1637670000000L, "BTC", 1);
        CryptoData crypto2 = new CryptoData(1637680000000L, "ETH", 2);
        CryptoData crypto3 = new CryptoData(1637690000000L, "XRP", 3);

        List<CryptoData> cryptoDataList = new ArrayList<>();
        cryptoDataList.add(crypto1);
        cryptoDataList.add(crypto2);
        cryptoDataList.add(crypto3);

        CryptoService cryptoServiceMock = mock(CryptoService.class);
        when(cryptoServiceMock.readCryptoData("BTC")).thenReturn(cryptoDataList);

        long startTimestamp = 1637670000000L;
        long endTimestamp = 1637680000000L;

        List<CryptoData> filteredCryptoDataList = cryptoServiceMock.getCryptoDataForDay("BTC", startTimestamp, endTimestamp);

        assertEquals(1, filteredCryptoDataList.size());
        assertEquals(crypto1, filteredCryptoDataList.get(0));
    }

    @Test
    void testGetMinCryptoPrice() {
		 List<CryptoData> testData = new ArrayList<>();
	     testData.add(new CryptoData(1637670000000L, "BTC", 1));
	     testData.add(new CryptoData(1637680000000L, "ETH", 2));
	     testData.add(new CryptoData(1637690000000L, "XRP", 3));
	
	     CryptoService cryptoService = new CryptoService();
	
	     CryptoData result = cryptoService.getMinCryptoPrice(testData);
	
	     assertEquals("BTC", result.getCryptoSymbol());
	     assertEquals(1, result.getPrice());
    }

    @Test
    void testGetMaxCryptoPrice() {
    	List<CryptoData> testData = new ArrayList<>();
        testData.add(new CryptoData(1637670000000L, "BTC", 1));
        testData.add(new CryptoData(1637680000000L, "ETH", 2));
        testData.add(new CryptoData(1637690000000L, "XRP", 3));

        CryptoService cryptoService = new CryptoService();

        CryptoData result = cryptoService.getMaxCryptoPrice(testData);

        assertEquals("XRP", result.getCryptoSymbol());
        assertEquals(3, result.getPrice());
    }

    @Test
    void testGetOldestCrypto() {
    	List<CryptoData> testData = new ArrayList<>();
        testData.add(new CryptoData(1637670000000L, "BTC", 1));
        testData.add(new CryptoData(1637680000000L, "ETH", 2));
        testData.add(new CryptoData(1637690000000L, "XRP", 3));

        CryptoService cryptoService = new CryptoService();

        CryptoData result = cryptoService.getOldestCrypto(testData);

        assertEquals("BTC", result.getCryptoSymbol()); 
        assertEquals(1637670000000L, result.getTimeStamp());
    }

    @Test
    void testGetNewestCrypto() {
    	List<CryptoData> testData = new ArrayList<>();
        testData.add(new CryptoData(1637670000000L, "BTC", 1));
        testData.add(new CryptoData(1637680000000L, "ETH", 2));
        testData.add(new CryptoData(1637690000000L, "XRP", 3));

        CryptoService cryptoService = new CryptoService();

        CryptoData result = cryptoService.getNewestCrypto(testData);


        assertEquals("XRP", result.getCryptoSymbol()); 
        assertEquals(1637690000000L, result.getTimeStamp());
    }

}
