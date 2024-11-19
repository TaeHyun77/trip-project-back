package com.example.demo0810.repository.post;

import com.example.demo0810.Entity.post.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface PostRepository extends JpaRepository<PostEntity, Long> {

    @Transactional
    @Modifying
    @Query("update PostEntity b set b.count = b.count + 1 where b.id = :id")
    void updateCount(@Param("id") Long id);

}
