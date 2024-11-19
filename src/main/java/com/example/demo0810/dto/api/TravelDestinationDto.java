package com.example.demo0810.dto.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TravelDestinationDto {
    private int keywordRank;
    private String searchWord;
    private String upperCategory;
    private String lowerCategory;
    private String areaOrCountryName;
}
