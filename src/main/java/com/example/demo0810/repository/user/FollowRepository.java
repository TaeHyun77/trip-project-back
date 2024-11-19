package com.example.demo0810.repository.user;

import com.example.demo0810.Entity.user.follow.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    Optional<Follow> findByFollowName(String FollowName);


}
