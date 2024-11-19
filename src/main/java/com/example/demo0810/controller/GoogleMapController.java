package com.example.demo0810.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/google")
public class GoogleMapController {

    @Value("${google.api.key}")
    private String googleApiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    private ResponseEntity<String> callGoogleMapsApi(String endpoint, String queryParamName, String queryValue) {
        URI uri = UriComponentsBuilder
                .fromUriString("https://maps.googleapis.com")
                .path("/maps/api/" + endpoint + "/json")
                .queryParam(queryParamName, queryValue)
                .queryParam("key", googleApiKey)
                .encode(StandardCharsets.UTF_8)
                .build()
                .toUri();

        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedTime = now.format(formatter);
            System.out.println("[" + formattedTime + "] Google " + endpoint + " API를 성공적으로 불러왔습니다.");

            return ResponseEntity.ok(responseEntity.getBody());

        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"errorMessage\": \"API 호출 중 오류가 발생했습니다. " + e.getMessage() + "\"}");
        }
    }

    // Google 장소 검색 API 호출
    @GetMapping(value = "/search", produces = "application/json; charset=UTF-8")
    public ResponseEntity<String> searchPlaces(@RequestParam(name = "keyword") String keyword) {
        return callGoogleMapsApi("place/textsearch", "query", keyword);
    }

    // Google Geocoding API 호출
    @GetMapping(value = "/geocode", produces = "application/json; charset=UTF-8")
    public ResponseEntity<String> geocode(@RequestParam(name = "address") String address) {
        return callGoogleMapsApi("geocode", "address", address);
    }

    // Google Directions API 호출 ( 일단 보류 )
    @GetMapping(value = "/route", produces = "application/json; charset=UTF-8")
    public ResponseEntity<String> getRoute(@RequestParam(name = "origin") String origin,
                                           @RequestParam(name = "destination") String destination) {
        // origin과 destination을 각각의 쿼리 매개변수로 전달
        URI uri = UriComponentsBuilder
                .fromUriString("https://maps.googleapis.com")
                .path("/maps/api/directions/json")
                .queryParam("origin", origin)
                .queryParam("destination", destination)
                .queryParam("key", googleApiKey)
                .encode(StandardCharsets.UTF_8)
                .build()
                .toUri();

        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedTime = now.format(formatter);
            System.out.println("[" + formattedTime + "] Google Directions API를 성공적으로 불러왔습니다.");

            return ResponseEntity.ok(responseEntity.getBody());

        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"errorMessage\": \"API 호출 중 오류가 발생했습니다. " + e.getMessage() + "\"}");
        }
    }
}
