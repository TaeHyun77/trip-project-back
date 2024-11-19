package com.example.demo0810.Entity.etc;

import com.example.demo0810.dto.weather.WeatherDto;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Region {

    @Id
    @Column(name = "region_id")
    private Long id;

    @Column(name = "region_parent")
    private String parentRegion;

    @Column(name = "region_child")
    private String childRegion;

    private int nx;

    private int ny;

    @Embedded
    private WeatherDto weather;

    // 날씨 정보 제외하고 지역 생성
    public Region(Long id, String parentRegion, String childRegion, int nx, int ny) {
        this.id = id;
        this.parentRegion = parentRegion;
        this.childRegion = childRegion;
        this.nx = nx;
        this.ny = ny;
    }

    // 날씨 갱신
    public void updateRegionWeather(WeatherDto weather) {
        this.weather = weather;
    }


    @Override
    public String toString() {
        return parentRegion + " " + childRegion;
    }
}
