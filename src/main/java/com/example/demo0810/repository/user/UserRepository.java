package com.example.demo0810.repository.user;

import com.example.demo0810.Entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository <UserEntity, Long> {

    Boolean existsByUsername(String username);

    UserEntity findByUsername(String username);

    UserEntity findByName(String name);
}
