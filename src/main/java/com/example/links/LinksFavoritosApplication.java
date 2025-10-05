package com.example.links;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class LinksFavoritosApplication {

	public static void main(String[] args) {
		SpringApplication.run(LinksFavoritosApplication.class, args);
	}

}
