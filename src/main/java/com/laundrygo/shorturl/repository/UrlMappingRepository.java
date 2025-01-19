package com.laundrygo.shorturl.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import com.laundrygo.shorturl.domain.UrlMapping;

public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long> {
	Optional<UrlMapping> findByOriUrl(String oriUrl);
	Optional<UrlMapping> findByShortUrl(String shortUrl);
}
