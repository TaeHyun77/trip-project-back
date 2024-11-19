package com.example.demo0810.dto.user;

import com.example.demo0810.Entity.user.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDto {

    private String name;
    private String email;
    private String gender;
    private String age;
    private String selfIntro;

    public UserEntity UpdateUserEntity() {
        return UserEntity.builder()
                .name(name)
                .email(email)
                .gender(gender)
                .age(age)
                .selfIntro(selfIntro)
                .build();
    }
}

