package com.example.demo0810.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@CrossOrigin(origins = {"http://localhost:3000"})
@RestController
@RequestMapping("/kakao")
public class MapController {

    private final String kakaoApiKey = "dc880110e572c170182b3c1a63331de6"; // 카카오 API 키


    // 카카오 장소 검색 API 호출
    @GetMapping(value = "/search", produces = "application/json; charset=UTF-8")
    public ResponseEntity<String> searchPlaces(@RequestParam(name = "keyword") String keyword) {

        // 검색 창이 빈 경우
        if (keyword == null || keyword.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("{\"errorMessage\": \"검색어가 필요합니다.\"}");
        }

        // 카카오 API 요청 URI
        URI uri = UriComponentsBuilder
                .fromUriString("https://dapi.kakao.com")
                .path("/v2/local/search/keyword.json")
                .queryParam("query", keyword)
                .queryParam("size", 5) // 검색 수
                .encode(StandardCharsets.UTF_8)
                .build()
                .toUri();

        // 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoApiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // API 요청
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();

        try {
            // API 호출 및 응답 받기
            ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, String.class);
            String responseBody = responseEntity.getBody();

            // 로그: 현재 시간 기록
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedTime = now.format(formatter);

            // Kakao API 응답 로그
            System.out.println("[" + formattedTime + "] Kakao API를 성공적으로 불러왔습니다.");

            // 응답 리턴
            return ResponseEntity.ok(responseBody);
        } catch (Exception e) {
            // 예외 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"errorMessage\": \"API 호출 중 오류가 발생했습니다: " + e.getMessage() + "\"}");
        }
    }
}
