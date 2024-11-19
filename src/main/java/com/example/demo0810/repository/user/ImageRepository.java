package com.example.demo0810.repository.user;

import com.example.demo0810.Entity.etc.ImageEntity;
import com.example.demo0810.Entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<ImageEntity, Long> {

    ImageEntity findByUser(UserEntity user);
}