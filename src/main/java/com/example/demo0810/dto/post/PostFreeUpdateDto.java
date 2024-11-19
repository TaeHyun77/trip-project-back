package com.example.demo0810.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostFreeUpdateDto {

    private Long id;
    private String title;
    private String content;
    private String postImageUrl;

}
