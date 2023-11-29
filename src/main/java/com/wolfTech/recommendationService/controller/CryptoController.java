package com.wolfTech.recommendationService.controller;

import java.util.List;

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
		
		CryptoData cryptoData = cryptoService.minCryptoPrice(cryptoCurrency);	
		return new ResponseEntity<>(cryptoData, HttpStatus.OK);
	}
	
	@GetMapping("/specific_max_crypto_data")
    public ResponseEntity<CryptoData> getSpecificMaxCryptoData(@RequestParam("cryptoCurrency") String cryptoCurrency) {
		
		CryptoData cryptoData = cryptoService.maxCryptoPrice(cryptoCurrency);	
		return new ResponseEntity<>(cryptoData, HttpStatus.OK);
	}
	
	@GetMapping("/specific_oldest_crypto_data")
    public ResponseEntity<CryptoData> getSpecificOldestCryptoData(@RequestParam("cryptoCurrency") String cryptoCurrency) {
		
		CryptoData cryptoData = cryptoService.oldestCrypto(cryptoCurrency);	
		return new ResponseEntity<>(cryptoData, HttpStatus.OK);
	}
	
	@GetMapping("/specific_newest_crypto_data")
    public ResponseEntity<CryptoData> getSpecificNewestCryptoData(@RequestParam("cryptoCurrency") String cryptoCurrency) {
		
		CryptoData cryptoData = cryptoService.newestCrypto(cryptoCurrency);	
		return new ResponseEntity<>(cryptoData, HttpStatus.OK);
	}
}
