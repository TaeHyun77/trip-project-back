package com.example.demo0810.dto.weather;

import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@Builder
public class CombineWeatherDto {
    private TodayWeatherDto currentWeather;
    private List<WeeklyWeatherDto> weeklyWeatherList;
}