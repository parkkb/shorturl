package com.laundrygo.shorturl.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.laundrygo.shorturl.domain.UrlMapping;
import com.laundrygo.shorturl.repository.UrlMappingRepository;

public class UrlMappingServiceTest {

	private UrlMappingRepository repository;
	private UrlMappingService service;

	@BeforeEach
	void setUp() {
		repository = Mockito.mock(UrlMappingRepository.class);
		service = new UrlMappingService(repository);
	}

	@Test
	void testShortenUrl_NewUrl() {
		String oriUrl = "https://example.com";
		String expectedShortUrl = "abc12345";

		when(repository.findByOriUrl(oriUrl)).thenReturn(Optional.empty());
		when(repository.findByShortUrl(anyString())).thenReturn(Optional.empty());

		String shortUrl = service.shortUrl(oriUrl);

		// Verify and assert
		assertNotNull(shortUrl);
		assertTrue(shortUrl.length() <= 8);
		verify(repository, times(1)).save(any(UrlMapping.class));
	}

	@Test
	void testShortenUrl_ExistingUrl() {
		String oriUrl = "https://example.com";
		String expectedShortUrl = "abc12345";


		UrlMapping existingMapping = new UrlMapping();
		existingMapping.setOriUrl(oriUrl);
		existingMapping.setShortUrl(expectedShortUrl);

		when(repository.findByOriUrl(oriUrl)).thenReturn(Optional.of(existingMapping));

		String shortUrl = service.shortUrl(oriUrl);

		assertEquals(expectedShortUrl, shortUrl);
		verify(repository, never()).save(any(UrlMapping.class));
	}

	@Test
	void testGetOriginalUrl_ValidShortUrl() {
		String shortUrl = "abc12345";
		String oriUrl = "https://example.com";

		UrlMapping mapping = new UrlMapping();
		mapping.setOriUrl(oriUrl);
		mapping.setShortUrl(shortUrl);
		mapping.setRequestCount(0);

		when(repository.findByShortUrl(shortUrl)).thenReturn(Optional.of(mapping));

		String retrievedOriUrl = service.getOriginalUrl(shortUrl);

		assertEquals(oriUrl, retrievedOriUrl);
		assertEquals(1, mapping.getRequestCount());
		verify(repository, times(1)).save(mapping);
	}

	@Test
	void testGetOriginalUrl_InvalidShortUrl() {
		String shortUrl = "invalid";

		when(repository.findByShortUrl(shortUrl)).thenReturn(Optional.empty());

		assertThrows(IllegalArgumentException.class, () -> service.getOriginalUrl(shortUrl));
	}
}
