package com.wolfTech.recommendationService.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.wolfTech.recommendationService.model.CryptoData;

@Service
public class CryptoService {
	private static final String PRICES_DIRECTORY = "src/main/resources/prices/";
	
	public Map<String, Double> readAllCryptoData(){
		List<CryptoData> allCryptoDataList = new ArrayList<>();
		Map<String, Double> normalizedCryptoList = new HashMap<String, Double>();
		
		try (DirectoryStream<Path> pricesDirectoryStream = Files.newDirectoryStream(Paths.get(PRICES_DIRECTORY), "*.csv")) {
			for (Path csvPath : pricesDirectoryStream) {
				String cryptoSymbol = csvPath.getFileName().toString().split("_")[0];
				double normalizedRange = (maxCryptoPrice(cryptoSymbol).getPrice() - minCryptoPrice(cryptoSymbol).getPrice()) / minCryptoPrice(cryptoSymbol).getPrice();
				normalizedCryptoList.put(cryptoSymbol, normalizedRange);
			}
			//TODO Finish this hasmap thing to get the sorted data
			normalizedCryptoList.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).collect(Collectors.toList());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return allCryptoDataList;
	}
	
	public List<CryptoData> readCryptoData(String specifiedCrypto){
		List<CryptoData> cryptoDataList = new ArrayList<>();
		
		try (BufferedReader br = new BufferedReader(new FileReader(PRICES_DIRECTORY + specifiedCrypto + "_values.csv"))) {
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
			CryptoData unknownCrypto = new CryptoData(0, "unknown", 0);
			cryptoDataList.add(unknownCrypto);
			return cryptoDataList;
		}
		return cryptoDataList;
	}
	
	public CryptoData minCryptoPrice(String crypto) {
		CryptoData minCryptoData = new CryptoData(0, "unknown", 0);
		List<CryptoData> cryptoDataList = readCryptoData(crypto);
		double minPrice = cryptoDataList.get(0).getPrice();

		for (CryptoData cryptoData : cryptoDataList) {
			if (cryptoData.getPrice() < minPrice) {
				minPrice = cryptoData.getPrice();
				minCryptoData = cryptoData;
			}
		}
		return minCryptoData;
	}
	
	public CryptoData maxCryptoPrice(String crypto) {
		CryptoData maxCryptoData = new CryptoData(0, "unknown", 0);
		List<CryptoData> cryptoDataList = readCryptoData(crypto);
		double maxPrice = 0.0;
		
		for (CryptoData cryptoData : cryptoDataList) {
			if (cryptoData.getPrice() > maxPrice) {
				maxPrice = cryptoData.getPrice();
				maxCryptoData = cryptoData;
			}
		}
		return maxCryptoData;
	}
	
	public CryptoData oldestCrypto(String crypto) {
		CryptoData oldestCryptoData = new CryptoData(0, "unknown", 0);
		List<CryptoData> cryptoDataList = readCryptoData(crypto);
		long oldestTimestamp = Long.MAX_VALUE;
		
		for(CryptoData cryptodata : cryptoDataList) {
			if (cryptodata.getTimeStamp() < oldestTimestamp) {
				oldestTimestamp = cryptodata.getTimeStamp();
				oldestCryptoData = cryptodata;
				System.out.println("im here");
			}
		}
		return oldestCryptoData;
	}
	
	public CryptoData newestCrypto(String crypto) {
		CryptoData newestCryptoData = new CryptoData(0, "unknown", 0);
		List<CryptoData> cryptoDataList = readCryptoData(crypto);
		long newestTimestamp = Long.MIN_VALUE;
		
		for(CryptoData cryptodata : cryptoDataList) {
			if (cryptodata.getTimeStamp() > newestTimestamp) {
				newestTimestamp = cryptodata.getTimeStamp();
				newestCryptoData = cryptodata;
			}
		}
		return newestCryptoData;
	}
}
