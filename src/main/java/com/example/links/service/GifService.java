package com.example.links.service;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;

@Service
public class GifService {

    @Value("${gif.api.key}")
    private String apiKey;

    private static final String GIPHY_URL = "https://api.giphy.com/v1/gifs/random";

    @Cacheable(value = "randomGif")
    public String getRandomGif() {
        try {
            String url = UriComponentsBuilder.fromUriString(GIPHY_URL)
                    .queryParam("api_key", apiKey)
                    .queryParam("tag", "br")
                    .queryParam("rating", "g")
                    .toUriString();

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(java.net.URI.create(url))
                    .GET()
                    .build();
            HttpResponse<String> httpResponse = client.send(request,
                    java.net.http.HttpResponse.BodyHandlers.ofString());

            JsonNode root = new com.fasterxml.jackson.databind.ObjectMapper()
                    .readTree(httpResponse.body());
            JsonNode original = root.path("data").path("images").path("original");
            return original.path("url").asText();
        } catch (Exception e) {
            throw new IllegalStateException("Erro ao buscar GIF aleatório", e);
        }
    }

    @CacheEvict(value = { "randomGif" })
    public void wipeCache() {
        System.out.println("Limpando cache da imagem...");
    }
}
