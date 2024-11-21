package com.example.demo0810.dto.comment;

import com.example.demo0810.Entity.post.CommentEntity;
import com.example.demo0810.Entity.post.PostEntity;
import com.example.demo0810.Entity.user.UserEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentRequestDto {

    private String comment;
    private UserEntity user;
    private PostEntity post;
    private String author;

    @Builder
    public CommentRequestDto(String comment, UserEntity user, PostEntity post, String author) {
        this.comment = comment;
        this.user = user;
        this.post = post;
        this.author = author;
    }

    public CommentEntity toComment() {
        return CommentEntity.builder()
                .comment(comment)
                .user(user)
                .post(post)
                .author(author)
                .build();
    }
}
