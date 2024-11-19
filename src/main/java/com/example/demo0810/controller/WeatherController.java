package com.example.demo0810.controller;

import com.example.demo0810.dto.weather.CombineWeatherDto;
import com.example.demo0810.dto.weather.TodayWeatherDto;
import com.example.demo0810.dto.weather.WeeklyWeatherDto;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/forecast")
public class WeatherController {

    @Value("${chromedriver.path}")
    private String chromedriverPath;

    @GetMapping("/weather")
    public ResponseEntity<CombineWeatherDto> getAllWeatherInformation(@RequestParam(name = "location", defaultValue = "현재위치") String location) {
        // ChromeDriver 경로 설정
        try {
            ClassPathResource resource = new ClassPathResource(chromedriverPath);
            File chromedriverFile = resource.getFile();
            System.setProperty("webdriver.chrome.driver", chromedriverFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("ChromeDriver 경로 설정 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless", "--disable-gpu", "--no-sandbox"); // 헤드리스 모드 비활성화

        WebDriver driver = new ChromeDriver(options);

        try {
            // URL 인코딩
            String encLocation = URLEncoder.encode(location + " 날씨", StandardCharsets.UTF_8);
            String url = "https://search.naver.com/search.naver?ie=utf8&query=" + encLocation;

            driver.get(url);
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

            // 오늘 날씨 정보 가져오기
            WebElement todayWeather = driver.findElement(By.className("status_wrap"));
            WebElement locationElement = driver.findElement(By.xpath("//div[@class='top_wrap']//h2[@class='title']"));
            String savelocation = locationElement.getText();
            String condition = todayWeather.findElement(By.cssSelector("span.weather.before_slash")).getText();
            String temperature = todayWeather.findElement(By.className("temperature_text"))
                    .findElement(By.tagName("strong")).getText().replaceAll("[^0-9.]", "");
            String humidity = todayWeather.findElement(By.xpath("//dt[text()='습도']/following-sibling::dd")).getText();
            String feelsLike = todayWeather.findElement(By.xpath("//dt[text()='체감']/following-sibling::dd")).getText();
            String windSpeed = todayWeather.findElement(By.xpath("//dt[contains(text(),'풍')]/following-sibling::dd")).getText();

            TodayWeatherDto currentWeather = TodayWeatherDto.builder()
                    .condition(condition)
                    .temperature(temperature)
                    .location(location)
                    .savelocation(savelocation)
                    .humidity(humidity)
                    .feelsLike(feelsLike)
                    .windSpeed(windSpeed)
                    .build();

            // 주간 예보 정보 가져오기
            List<WeeklyWeatherDto> weeklyWeatherList = new ArrayList<>();
            List<WebElement> weekItems = driver.findElements(By.cssSelector(".week_list > .week_item"));

            for (WebElement weekItem : weekItems) {
                String day = weekItem.findElement(By.className("day")).getText();
                String date = weekItem.findElement(By.className("date")).getText();

                // 오전 날씨
                try {
                    WebElement amWeatherElement = weekItem.findElements(By.className("weather_inner")).get(0);
                    String amCondition = amWeatherElement.findElement(By.className("blind")).getText();
                    String amRainfall = amWeatherElement.findElement(By.className("rainfall")).getText();

                    // 오후 날씨
                    WebElement pmWeatherElement = weekItem.findElements(By.className("weather_inner")).get(1);
                    String pmCondition = pmWeatherElement.findElement(By.className("blind")).getText();
                    String pmRainfall = pmWeatherElement.findElement(By.className("rainfall")).getText();

                    // 온도
                    String lowestTemp = weekItem.findElement(By.cssSelector(".temperature_inner > .lowest")).getText().replaceAll("[^0-9°]", "");
                    String highestTemp = weekItem.findElement(By.cssSelector(".temperature_inner > .highest")).getText().replaceAll("[^0-9°]", "");

                    weeklyWeatherList.add(WeeklyWeatherDto.builder()
                            .date(date)
                            .day(day)
                            .amWeather(amCondition + " (" + amRainfall + ")")
                            .pmWeather(pmCondition + " (" + pmRainfall + ")")
                            .lowestTemp(lowestTemp)
                            .highestTemp(highestTemp)
                            .build());
                } catch (Exception e) {
                    System.err.println("주간 날씨 정보 가져오는 중 오류 발생: " + e.getMessage());
                    e.printStackTrace();
                }
            }

            CombineWeatherDto combinedWeatherDto = CombineWeatherDto.builder()
                    .currentWeather(currentWeather)
                    .weeklyWeatherList(weeklyWeatherList)
                    .build();

            return new ResponseEntity<>(combinedWeatherDto, HttpStatus.OK);

        } catch (Exception e) {
            System.err.println("날씨 정보를 가져오는 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            driver.quit();
        }
    }
}
