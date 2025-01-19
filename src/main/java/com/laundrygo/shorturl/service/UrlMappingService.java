package com.laundrygo.shorturl.service;

import java.util.Optional;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.laundrygo.shorturl.domain.UrlMapping;
import com.laundrygo.shorturl.repository.UrlMappingRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UrlMappingService {
	private final UrlMappingRepository repository;
	private final Random random = new Random();

	public String shortUrl(String oriUrl) {
		Optional<UrlMapping> existingMapping = repository.findByOriUrl(oriUrl);
		if (existingMapping.isPresent()) {
			return existingMapping.get().getShortUrl();
		}

		String shortUrl = generateUniqueShortUrl();
		UrlMapping mapping = new UrlMapping();
		mapping.setOriUrl(oriUrl);
		mapping.setShortUrl(shortUrl);

		repository.save(mapping);
		return shortUrl;
	}

	public String getOriginalUrl(String shortUrl) {
		UrlMapping mapping = repository.findByShortUrl(shortUrl)
				.orElseThrow(() -> new IllegalArgumentException("Short URL not found"));

		// 요청 수 증가
		mapping.setRequestCount(mapping.getRequestCount() + 1);
		repository.save(mapping);

		return mapping.getOriUrl();
	}

	public Iterable<UrlMapping> getAllMappings() {
		return repository.findAll();
	}

	private String generateUniqueShortUrl() {
		String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		StringBuilder shortUrl;
		do {
			shortUrl = new StringBuilder();
			for (int i = 0; i < 8; i++) {
				shortUrl.append(characters.charAt(random.nextInt(characters.length())));
			}
		} while (repository.findByShortUrl(shortUrl.toString()).isPresent());
		return shortUrl.toString();
	}
}
