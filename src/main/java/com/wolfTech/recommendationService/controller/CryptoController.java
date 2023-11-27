package com.wolfTech.recommendationService.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wolfTech.recommendationService.model.CryptoData;
import com.wolfTech.recommendationService.service.CryptoService;

@RestController
@RequestMapping("/crypto")
public class CryptoController {
	@Autowired
    private CryptoService cryptoService;
	
	@GetMapping("/current_BTC_list")
    public ResponseEntity<List<CryptoData>> getCurrentBTCCryptoList() {
		List<CryptoData> cryptoBTC = cryptoService.readCryptoData();
		if (!cryptoBTC.isEmpty()) {
			return new ResponseEntity<>(cryptoBTC, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(cryptoBTC, HttpStatus.NO_CONTENT);
		}
	}
}
