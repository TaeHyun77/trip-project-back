package com.example.demo0810.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserResponseDto {
    private String username;
    private String role;
    private String category;
    private String name;
    private String email;
    private LocalDateTime createdDate;
    private String profileImageUrl;
    private String gender;
    private String age;
    private String selfIntro;
    private List<String> follow;

}


