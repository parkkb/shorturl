package com.laundrygo.shorturl.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.*;

import com.laundrygo.shorturl.domain.UrlMapping;
import com.laundrygo.shorturl.exception.InvalidUrlException;
import com.laundrygo.shorturl.service.UrlMappingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UrlMappingController {
	private final UrlMappingService service;


	@PostMapping("/shorten")
	public String shortenUrl(@RequestBody Map<String, String> request) {
		String oriUrl = request.get("oriUrl");
		if (oriUrl == null || oriUrl.isEmpty()) {
			throw new InvalidUrlException("Original URL must not be null or empty");
		}
		return service.shortUrl(oriUrl);
	}

	@GetMapping("/original")
	public String getOriginalUrl(@RequestParam String shortUrl) {
		return service.getOriginalUrl(shortUrl);
	}

	@GetMapping("/stats")
	public Iterable<UrlMapping> getStats() {
		return service.getAllMappings();
	}
}
