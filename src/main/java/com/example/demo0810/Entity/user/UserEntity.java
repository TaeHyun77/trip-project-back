package com.example.demo0810.Entity.user;

import com.example.demo0810.Entity.etc.BaseTimeEntity;
import com.example.demo0810.Entity.etc.ImageEntity;
import com.example.demo0810.Entity.user.follow.UserFollowMap;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.util.ArrayList;
import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class) // Auditing 기능 활성화
@Entity
public class UserEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    private String password;

    private String post;

    private String role;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    private String category;

    private String gender;

    private String age;

    private String selfIntro;

    @JsonIgnore
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ImageEntity image;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserFollowMap> userFollowMap = new ArrayList<>();

    @Builder
    public UserEntity(String username, String password, String role, String name, String email, String category, ImageEntity image, String gender, String age, String selfIntro) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.name = name;
        this.email = email;
        this.category = category;
        this.image = image;
        this.gender = gender;
        this.age = age;
        this.selfIntro = selfIntro;
    }

    public void updateUser(String name, String email, String gender, String age, String selfIntro) {
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.age = age;
        this.selfIntro = selfIntro;
    }
}