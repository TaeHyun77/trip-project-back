package com.example.demo0810.repository.user;

import com.example.demo0810.Entity.user.MessageEntity;
import com.example.demo0810.Entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<MessageEntity, Long> {

    List<MessageEntity> findAllByReceiver(UserEntity user);

    List<MessageEntity> findAllBySender(UserEntity user);

}
