package com.example.demo0810.repository.post;

import com.example.demo0810.Entity.post.PostEntity;
import com.example.demo0810.Entity.post.PostImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostImageRepository extends JpaRepository<PostImageEntity, Long> {

    PostImageEntity findByPost(PostEntity post);
}
