package com.example.demo0810.dto.weather;

import lombok.*;

@Getter
@Builder
public class TodayWeatherDto {
    private String condition;      // 날씨 상태 (맑음, 흐림 등등)
    private String temperature;    // 현재 온도
    private String location;       // 위치
    private String savelocation;   // 검색 위치
    private String humidity;       // 습도
    private String feelsLike;      // 체감 온도
    private String windSpeed;      // 바람 속도
}
