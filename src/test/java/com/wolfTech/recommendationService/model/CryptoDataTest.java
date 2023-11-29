package com.wolfTech.recommendationService.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CryptoDataTest {

	@Test
    void testGettersAndSetters() {
        CryptoData cryptoData = new CryptoData(1637842800000L, "BTC", 5);

        assertEquals(1637842800000L, cryptoData.getTimeStamp());
        assertEquals("BTC", cryptoData.getCryptoSymbol());
        assertEquals(5, cryptoData.getPrice());

        cryptoData.setTimeStamp(1637929200000L);
        cryptoData.setCryptoSymbol("ETH");
        cryptoData.setPrice(4);

        assertEquals(1637929200000L, cryptoData.getTimeStamp());
        assertEquals("ETH", cryptoData.getCryptoSymbol());
        assertEquals(4, cryptoData.getPrice());
    }

}
