package com.laundrygo.shorturl.domain;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class UrlMapping {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String oriUrl;

	@Column(nullable = false, unique = true, length = 8)
	private String shortUrl;

	@Column(nullable = false)
	private int requestCount = 0;
}
