package com.example.demo0810.dto.image;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

public class ImageResponseDto {

    @JsonProperty("url")
    private String url;

    @Builder
    public ImageResponseDto(String url) {
        this.url = url;
    }

}
