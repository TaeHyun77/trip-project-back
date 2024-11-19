package com.example.demo0810.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {

    private Long id;
    private String title;
    private String content;
    private String writer;
    private int count;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private String mbti;
    private String place;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<String> hashtags;
    private String postImageUrl;
    private int people;
    private String postCategory;
    private List<String> participation;
    private boolean status = false;
}

