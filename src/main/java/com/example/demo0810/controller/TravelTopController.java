package com.example.demo0810.controller;

import com.example.demo0810.dto.api.TravelDestinationDto;
import com.opencsv.CSVReader;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/travel")
@RequiredArgsConstructor
public class TravelTopController {

    private final ResourceLoader resourceLoader;

    @Value("${travel.domestic.csv.location}")
    private String domesticCsvFilePath; // 국내 여행 CSV 파일 경로

    @Value("${travel.international.csv.location}")
    private String internationalCsvFilePath; // 해외 여행 CSV 파일 경로

    @GetMapping("/top10/domestic")
    public List<TravelDestinationDto> getTop10DomesticDestinations() {
        List<TravelDestinationDto> destinations = readDomesticCsv(domesticCsvFilePath);
        return getTop10Destinations(destinations);
    }

    @GetMapping("/top10/international")
    public List<TravelDestinationDto> getTop10InternationalDestinations() {
        List<TravelDestinationDto> destinations = readInternationalCsv(internationalCsvFilePath);
        return getTop10Destinations(destinations);
    }

    private List<TravelDestinationDto> readDomesticCsv(String filePath) {
        List<TravelDestinationDto> destinations = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new InputStreamReader(
                resourceLoader.getResource(filePath).getInputStream()))) {
            String[] values;
            csvReader.readNext(); // 헤더 건너뛰기
            while ((values = csvReader.readNext()) != null) {
                // 데이터 읽기 및 DTO 생성
                int keywordRank = Integer.parseInt(values[0]); // ALL_KWRD_RANK_CO
                String searchWord = values[1]; // SRCHWRD_NM
                String upperCategory = values[2]; // UPPER_CTGRY_NM
                String lowerCategory = values[3]; // LWPRT_CTGRY_NM
                String areaName = values[4]; // AREA_NM

                // TravelDestinationDto 객체 생성
                TravelDestinationDto destinationDto = new TravelDestinationDto(keywordRank, searchWord, upperCategory, lowerCategory, areaName);
                destinations.add(destinationDto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return destinations;
    }

    private List<TravelDestinationDto> readInternationalCsv(String filePath) {
        List<TravelDestinationDto> destinations = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new InputStreamReader(
                resourceLoader.getResource(filePath).getInputStream()))) {
            String[] values;
            csvReader.readNext(); // 헤더 건너뛰기
            while ((values = csvReader.readNext()) != null) {
                // 데이터 읽기 및 DTO 생성
                int keywordRank = Integer.parseInt(values[0]); // ALL_KWRD_RANK_CO
                String searchWord = values[1]; // SRCHWRD_NM
                String upperCategory = values[2]; // UPPER_CTGRY_NM
                String lowerCategory = values[3]; // LWPRT_CTGRY_NM
                String countryName = values[4]; // COUNTRY_NM

                // TravelDestinationDto 객체 생성
                TravelDestinationDto destinationDto = new TravelDestinationDto(keywordRank, searchWord, upperCategory, lowerCategory, countryName);
                destinations.add(destinationDto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return destinations;
    }

    private List<TravelDestinationDto> getTop10Destinations(List<TravelDestinationDto> destinations) {
        return destinations.stream()
                .sorted(Comparator.comparingInt(TravelDestinationDto::getKeywordRank)) // 키워드 순위로 정렬 (오름차순)
                .limit(10)
                .collect(Collectors.toList());
    }
}
