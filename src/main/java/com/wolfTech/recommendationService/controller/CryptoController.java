package com.wolfTech.recommendationService.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.wolfTech.recommendationService.model.CryptoData;
import com.wolfTech.recommendationService.service.CryptoService;

@RestController
@RequestMapping("/")
public class CryptoController {
	@Autowired
    private CryptoService cryptoService;
	
	@GetMapping("/")
	public RedirectView chosenDefaultPath() {
		return new RedirectView("/all_crypto_list");
	}
	
	@GetMapping("/all_crypto_list")
    public ResponseEntity<List<CryptoData>> getAllCryptoList() {
		List<CryptoData> allCrypto = cryptoService.readAllCryptoData();
		if (!allCrypto.isEmpty()) {
			return new ResponseEntity<>(allCrypto, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(allCrypto, HttpStatus.NO_CONTENT);
		}
	}
	
	@GetMapping("/sort_norm_crypto")
    public ResponseEntity<Map<String, Double>> getSortNormCryptos() {
		Map<String, Double> sortNormCrypto = cryptoService.getSortedNormalizedCryptos();
		if (!sortNormCrypto.isEmpty()) {
			return new ResponseEntity<>(sortNormCrypto, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(sortNormCrypto, HttpStatus.NO_CONTENT);
		}
	}
	
	@GetMapping("/specific_crypto_data")
    public ResponseEntity<List<CryptoData>> getSpecificCryptoData(@RequestParam("cryptoCurrency") String cryptoCurrency) {
		List<CryptoData> cryptoData = cryptoService.readCryptoData(cryptoCurrency);
		if (!cryptoData.isEmpty()) {
			return new ResponseEntity<>(cryptoData, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(cryptoData, HttpStatus.NO_CONTENT);
		}
	}
	
	@GetMapping("/specific_min_crypto_data")
    public ResponseEntity<CryptoData> getSpecificMinCryptoData(@RequestParam("cryptoCurrency") String cryptoCurrency) {
		
		CryptoData cryptoData = cryptoService.getMinCryptoPrice(cryptoService.readCryptoData(cryptoCurrency));	
		return new ResponseEntity<>(cryptoData, HttpStatus.OK);
	}
	
	@GetMapping("/specific_max_crypto_data")
    public ResponseEntity<CryptoData> getSpecificMaxCryptoData(@RequestParam("cryptoCurrency") String cryptoCurrency) {
		
		CryptoData cryptoData = cryptoService.getMaxCryptoPrice(cryptoService.readCryptoData(cryptoCurrency));	
		return new ResponseEntity<>(cryptoData, HttpStatus.OK);
	}
	
	@GetMapping("/specific_oldest_crypto_data")
    public ResponseEntity<CryptoData> getSpecificOldestCryptoData(@RequestParam("cryptoCurrency") String cryptoCurrency) {
		
		CryptoData cryptoData = cryptoService.getOldestCrypto(cryptoService.readCryptoData(cryptoCurrency));	
		return new ResponseEntity<>(cryptoData, HttpStatus.OK);
	}
	
	@GetMapping("/specific_newest_crypto_data")
    public ResponseEntity<CryptoData> getSpecificNewestCryptoData(@RequestParam("cryptoCurrency") String cryptoCurrency) {
		
		CryptoData cryptoData = cryptoService.getNewestCrypto(cryptoService.readCryptoData(cryptoCurrency));	
		return new ResponseEntity<>(cryptoData, HttpStatus.OK);
	}
	
	@GetMapping("/oldnewmimax_crypto_data")
    public ResponseEntity<List<CryptoData>> getOldNewMinMaxForCryptoData(@RequestParam("cryptoCurrency") String cryptoCurrency) {
		List<CryptoData> cryptoData = cryptoService.getOldestNewestMinMaxForCrypto(cryptoCurrency);
		if (!cryptoData.isEmpty()) {
			return new ResponseEntity<>(cryptoData, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(cryptoData, HttpStatus.NO_CONTENT);
		}
	}
	
	@GetMapping("/get_highest_crypto_for_day")
    public ResponseEntity<Map.Entry<String, Double>> getHighestCryptoForDay(@RequestParam("date") String date) {
		
		Map.Entry<String, Double> highestCrypto = cryptoService.getHighestNormCryptoForDay(date);	
		return new ResponseEntity<>(highestCrypto, HttpStatus.OK);
	}
	
	@GetMapping("/get_highest_crypto_for_date_range")
    public ResponseEntity<Map.Entry<String, Double>> getHighestCryptoForDay(
    		@RequestParam("startDate") String startDate,
    		@RequestParam("endDate") String endDate) {
		
		Map.Entry<String, Double> highestCrypto = cryptoService.getHighestNormCryptoForDateRange(startDate, endDate);	
		return new ResponseEntity<>(highestCrypto, HttpStatus.OK);
	}
}
