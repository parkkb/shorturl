package com.laundrygo.shorturl.controller;

import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.web.servlet.function.RequestPredicates.*;

import com.laundrygo.shorturl.service.UrlMappingService;

@WebMvcTest(UrlMappingController.class)
public class UrlMappingControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UrlMappingService service;

	@Test
	void testShortenUrl() throws Exception {
		String oriUrl = "https://example.com";
		String shortUrl = "abc12345";

		when(service.shortUrl(oriUrl)).thenReturn(shortUrl);

		mockMvc.perform(post("/shorten")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"oriUrl\": \"https://example.com\"}"))
				.andExpect(status().isOk())
				.andExpect(content().string(shortUrl));

		verify(service, times(1)).shortUrl(oriUrl);
	}
}
