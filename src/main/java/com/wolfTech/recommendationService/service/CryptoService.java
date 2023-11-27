package com.wolfTech.recommendationService.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import com.wolfTech.recommendationService.model.CryptoData;

@Service
public class CryptoService {
	private static final String PRICES_DIRECTORY = "src/main/resources/prices/BTC_values.csv";
	
	public List<CryptoData> readCryptoData(){
		List<CryptoData> cryptoDataList = new ArrayList<>();
		
		try (BufferedReader br = new BufferedReader(new FileReader(PRICES_DIRECTORY))) {
			String oneLineOfData;
			br.readLine();
			while ((oneLineOfData = br.readLine()) != null) {
				String[] obtainedData = oneLineOfData.split(",");
				if (obtainedData.length == 3) {
					long timeStamp = Long.parseLong(obtainedData[0]);
					String cryptoSymbol = obtainedData[1];
					double cryptoPrice = Double.parseDouble(obtainedData[2]);
					CryptoData cryptoData = new CryptoData(timeStamp, cryptoSymbol, cryptoPrice);
					cryptoDataList.add(cryptoData);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cryptoDataList;
	}
}
