package com.example.demo0810.Entity.post;

import com.example.demo0810.Entity.etc.BaseTimeEntity;
import com.example.demo0810.Entity.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "comment")
@Entity
public class CommentEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String comment;

    @Column
    private String author;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private PostEntity post;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    // 부모 댓글과의 관계 (ManyToOne)
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_comment_id")
    private CommentEntity parentComment;

    // 대댓글 목록 (OneToMany)
    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL)
    private List<CommentEntity> replies = new ArrayList<>();

    // 댓글 수정 메서드
    public void CommentUpdate(String comment) {
        this.comment = comment;
    }
}
