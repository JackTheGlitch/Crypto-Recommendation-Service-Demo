package com.wolfTech.recommendationService.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.wolfTech.recommendationService.model.CryptoData;

@Service
public class CryptoService {
	private static final String PRICES_DIRECTORY = "src/main/resources/prices/";
	
	//Looks at all the crypto, and puts it in a List<CryptoData> object. Not sorted in any way or matter, #chaos.
	public List<CryptoData> readAllCryptoData() {
		List<CryptoData> allCryptoDataList = new ArrayList<>();
		
		try (DirectoryStream<Path> pricesDirectoryStream = Files.newDirectoryStream(Paths.get(PRICES_DIRECTORY), "*.csv")) {
			for (Path csvPath : pricesDirectoryStream) {
				String cryptoFileName = csvPath.getFileName().toString().split("_")[0];
				allCryptoDataList.addAll(readCryptoData(cryptoFileName));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return allCryptoDataList;
	}
	
	//Looks at all of the crypto. Returns a hashmap in decending order with KEY:cryptoSymbol VALUE:normalizedValue.
	public Map<String, Double> getSortedNormalizedCryptos(){
		
		Map<String, Double> unSortedNormalizedCryptoMap = new HashMap<String, Double>();
		Map<String, Double> sortedNormalizedCryptoMap = new HashMap<String, Double>();
		List<String> cryptoSymbols = getAvailableCryptos();
		
		try {
			for (String cryptoSymbol : cryptoSymbols) {
				double normalizedValue = normalize(cryptoSymbol);
				unSortedNormalizedCryptoMap.put(cryptoSymbol, normalizedValue);
			}
			sortedNormalizedCryptoMap = getSortedNormalizedCryptoMap(unSortedNormalizedCryptoMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sortedNormalizedCryptoMap;
	}
	
	//Gets the highest normalized crypto for a day.
	public Map.Entry<String, Double> getHighestNormCryptoForDay(String date){
		Map<String, Double> unSortedNormalizedCryptoMap = new HashMap<String, Double>();
		Map<String, Double> sortedNormalizedCryptoMap = new HashMap<String, Double>();
		List<String> cryptoSymbols = getAvailableCryptos();
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate specifiedDate = LocalDate.parse(date, formatter);
        long startTimestamp = specifiedDate.atStartOfDay(ZoneOffset.UTC).toEpochSecond() * 1000;
        long endTimestamp = specifiedDate.plusDays(1).atStartOfDay(ZoneOffset.UTC).toEpochSecond() * 1000;
		
		try {
			for (String cryptoSymbol : cryptoSymbols) {
				double normalizedValue = normalize(cryptoSymbol, startTimestamp, endTimestamp);
				unSortedNormalizedCryptoMap.put(cryptoSymbol, normalizedValue);
			}
			sortedNormalizedCryptoMap = getSortedNormalizedCryptoMap(unSortedNormalizedCryptoMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map.Entry<String, Double> highest = sortedNormalizedCryptoMap.entrySet().iterator().next();
		return highest;
	}
	
	//Gets the highest normalized crypto for a date range.
	public Map.Entry<String, Double> getHighestNormCryptoForDateRange(String startDate, String endDate){
		Map<String, Double> unSortedNormalizedCryptoMap = new HashMap<String, Double>();
		Map<String, Double> sortedNormalizedCryptoMap = new HashMap<String, Double>();
		List<String> cryptoSymbols = getAvailableCryptos();
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        long startTimestamp = LocalDate.parse(startDate, formatter).atStartOfDay(ZoneOffset.UTC).toEpochSecond() * 1000;
        long endTimestamp = LocalDate.parse(endDate, formatter).atStartOfDay(ZoneOffset.UTC).toEpochSecond() * 1000;
		
		try {
			for (String cryptoSymbol : cryptoSymbols) {
				double normalizedValue = normalize(cryptoSymbol, startTimestamp, endTimestamp);
				unSortedNormalizedCryptoMap.put(cryptoSymbol, normalizedValue);
			}
			sortedNormalizedCryptoMap = getSortedNormalizedCryptoMap(unSortedNormalizedCryptoMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("MAP= " + sortedNormalizedCryptoMap);
		Map.Entry<String, Double> highest = sortedNormalizedCryptoMap.entrySet().iterator().next();
		return highest;
	}
	
	//Looks for a specifiedCrypto and returns a List<CryptoData> object
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
	
	//Receives a hashmap of KEY:cryptoSymbol and VALUE:normalizedValue. Just sorts it.
	public Map<String, Double> getSortedNormalizedCryptoMap (Map<String, Double> unSortedNormalizedCryptoMap){
		Map<String, Double> sortedNormalizedCryptoMap = unSortedNormalizedCryptoMap.entrySet()
				 .stream()
				 .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
				 .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
		return sortedNormalizedCryptoMap;
	}
	
	//Returns a List<String> object with the available cryptos. Depends on what the prices directory contains.
	public List<String> getAvailableCryptos() {
		List<String> cryptoSymbolArray = new ArrayList<>();
		try (DirectoryStream<Path> pricesDirectoryStream = Files.newDirectoryStream(Paths.get(PRICES_DIRECTORY), "*.csv")) {
			for (Path csvPath : pricesDirectoryStream) {
				String cryptoSymbol = csvPath.getFileName().toString().split("_")[0];
				cryptoSymbolArray.add(cryptoSymbol);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cryptoSymbolArray;
	}
	
	//Calculates the normalized range for a specific crypto.
	public double normalize(String symbol){
		List<CryptoData> cryptoDataList = readCryptoData(symbol);
		double normalizedValue = 0.0;
		
		CryptoData maxCrypto = getMaxCryptoPrice(cryptoDataList);
		CryptoData minCrypto = getMinCryptoPrice(cryptoDataList);
		
		normalizedValue = (maxCrypto.getPrice() - minCrypto.getPrice()) / minCrypto.getPrice();
		
		return normalizedValue;
		
	}
	
	//Calculates normalized range for a crypto between dates.
	public double normalize(String symbol, long startTimestamp, long endTimestamp){
		List<CryptoData> filteredCryptoDataList = getCryptoDataForDay(symbol, startTimestamp, endTimestamp);
		double normalizedValue = 0.0;
		
		if (!filteredCryptoDataList.isEmpty()) {
			CryptoData maxCrypto = getMaxCryptoPrice(filteredCryptoDataList);
			CryptoData minCrypto = getMinCryptoPrice(filteredCryptoDataList);
			
			normalizedValue = (maxCrypto.getPrice() - minCrypto.getPrice()) / minCrypto.getPrice();
			
			return normalizedValue;	
		}	
		return normalizedValue;
	}
	
	//Returns a List<CryptoData> object with crypto data between two timestamps.
	public List<CryptoData> getCryptoDataForDay(String symbol, long startTimestamp, long endTimestamp){
		List<CryptoData> cryptoDataList = readCryptoData(symbol);
		List<CryptoData> filteredCryptoDataList = new ArrayList<>();
		
		for (CryptoData cryptoData : cryptoDataList) {
			if (cryptoData.getTimeStamp() >= startTimestamp && cryptoData.getTimeStamp() < endTimestamp) {
				filteredCryptoDataList.add(cryptoData);
			}
		}
		return filteredCryptoDataList;
	}
	
	//Returns a CryptoData object with the smallest price.
	public CryptoData getMinCryptoPrice(List<CryptoData> cryptoDataList) {
		CryptoData minCryptoData = new CryptoData(0, "unknown", 0);
		double minPrice = cryptoDataList.get(0).getPrice();
		
		for (CryptoData cryptoData : cryptoDataList) {
			if (cryptoData.getPrice() <= minPrice) {
				minPrice = cryptoData.getPrice();
				minCryptoData = cryptoData;
			}
		}
		return minCryptoData;
	}
	
	//Returns a CryptodData object with the max price.
	public CryptoData getMaxCryptoPrice(List<CryptoData> cryptoDataList) {
		CryptoData maxCryptoData = new CryptoData(0, "unknown", 0);
		double maxPrice = 0.0;
		
		for (CryptoData cryptoData : cryptoDataList) {
			if (cryptoData.getPrice() > maxPrice) {
				maxPrice = cryptoData.getPrice();
				maxCryptoData = cryptoData;
			}
		}
		return maxCryptoData;
	}
	
	//Returns a CryptoData object with the biggest timestamp.
	public CryptoData getOldestCrypto(List<CryptoData> cryptoDataList) {
		CryptoData oldestCryptoData = new CryptoData(0, "unknown", 0);
		long oldestTimestamp = Long.MAX_VALUE;
		
		for(CryptoData cryptodata : cryptoDataList) {
			if (cryptodata.getTimeStamp() < oldestTimestamp) {
				oldestTimestamp = cryptodata.getTimeStamp();
				oldestCryptoData = cryptodata;
			}
		}
		return oldestCryptoData;
	}
	
	//Returns a CryptoData object with the smallest timestamp.
	public CryptoData getNewestCrypto(List<CryptoData> cryptoDataList) {
		CryptoData newestCryptoData = new CryptoData(0, "unknown", 0);
		long newestTimestamp = Long.MIN_VALUE;
		
		for(CryptoData cryptodata : cryptoDataList) {
			if (cryptodata.getTimeStamp() > newestTimestamp) {
				newestTimestamp = cryptodata.getTimeStamp();
				newestCryptoData = cryptodata;
			}
		}
		return newestCryptoData;
	}
	
	//Returns a List<CryptoData> object with the order of: oldest, newest, min, max
	public List<CryptoData> getOldestNewestMinMaxForCrypto(String crypto) {
		if (crypto.isEmpty()) {
			return new ArrayList<>();
		}
		List<CryptoData> cryptoData = readCryptoData(crypto);
		List<CryptoData> oldestNewestMinMaxCryptoData = new ArrayList<>();
		oldestNewestMinMaxCryptoData.add(getOldestCrypto(cryptoData));
		oldestNewestMinMaxCryptoData.add(getNewestCrypto(cryptoData));
		oldestNewestMinMaxCryptoData.add(getMinCryptoPrice(cryptoData));
		oldestNewestMinMaxCryptoData.add(getMaxCryptoPrice(cryptoData));
		
		return oldestNewestMinMaxCryptoData;
	}
}
