package com.example.demo0810.dto.weather;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WeeklyWeatherDto {
    private String date; // 날짜
    private String day; // 요일
    private String amWeather; // 오전 날씨
    private String pmWeather; // 오후 날씨
    private String amRainfall; // 오전 강수 확률
    private String pmRainfall; // 오후 강수 확률
    private String lowTemp; // 최저 기온
    private String highTemp; // 최고 기온
    private String lowestTemp; // 최고 기온
    private String highestTemp; // 최고 기온
}
