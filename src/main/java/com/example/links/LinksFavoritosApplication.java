package com.example.links;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@EnableScheduling
public class LinksFavoritosApplication {

	public static void main(String[] args) {
		SpringApplication.run(LinksFavoritosApplication.class, args);
	}

}
