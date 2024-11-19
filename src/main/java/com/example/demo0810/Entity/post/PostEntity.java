package com.example.demo0810.Entity.post;

import com.example.demo0810.Entity.etc.BaseTimeEntity;
import com.example.demo0810.Entity.post.particapation.PostPartiMap;
import com.example.demo0810.Entity.post.hashTag.PostTagMap;
import com.example.demo0810.Entity.user.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "board")
@Entity
public class PostEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    private String writer;

    private String mbti;

    private String place;

    private LocalDate startDate;

    private LocalDate endDate;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PostTagMap> postTagMaps = new ArrayList<>();  // 태그와의 매핑 추가

    // 조회수
    private int count = 0;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @JsonIgnore
    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private PostImageEntity postImage;

    private int people;

    private String postCategory;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PostPartiMap> postPartiMap = new ArrayList<>();

    private boolean status = false;

    @Builder
    public PostEntity(String title, String content, String writer, String mbti, String place, LocalDate startDate, LocalDate endDate, PostImageEntity postImage, int people, String postCategory, boolean status) {
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.count = 0;
        this.mbti = mbti;
        this.place = place;
        this.startDate = startDate;
        this.endDate = endDate;
        this.postImage = postImage;
        this.people = people;
        this.postCategory = postCategory;
        this.status = status;
    }

    public void updateFreePost(String title, String content, String postImageUrl) {
        this.title = title;
        this.content = content;

        if (postImageUrl != null && !postImageUrl.isEmpty()) {
            this.postImage.setPostImageUrl(postImageUrl);
        }
    }

    public void updatePost(LocalDate startDate, LocalDate endDate, String title, String content, String mbti, String place, String postImageUrl) {

        this.startDate = startDate;
        this.endDate = endDate;
        this.title = title;
        this.content = content;
        this.mbti = mbti;
        this.place = place;

        if (postImageUrl != null && !postImageUrl.isEmpty()) {
            this.postImage.setPostImageUrl(postImageUrl);
        }
    }

}
