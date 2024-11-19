package com.example.demo0810.dto.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegionDto {
    private Long id;
    private String parentRegion;
    private String childRegion;
    private int nx;
    private int ny;
}
