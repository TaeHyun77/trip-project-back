package com.example.demo0810.dto;

import com.example.demo0810.Entity.MessageEntity;
import com.example.demo0810.Entity.user.UserEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class MessageDto {

    private Long id;

    private String title;

    private String content;

    private String senderName;

    private String receiverName;

    private boolean deletedBySender;

    private boolean deletedByReceiver;

    private LocalDateTime createdDate;

    @Builder
    public MessageDto(Long id, String title, String content, String senderName, String receiverName, boolean deletedBySender, boolean deletedByReceiver, LocalDateTime createdDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.senderName = senderName;
        this.receiverName = receiverName;
        this.deletedBySender = deletedBySender;
        this.deletedByReceiver = deletedByReceiver;
        this.createdDate = createdDate;
    }

    public MessageEntity toEntity(UserEntity sender, UserEntity receiver) {
        return MessageEntity.builder()
                .title(title)
                .content(content)
                .sender(sender)
                .receiver(receiver)
                .deletedBySender(deletedBySender)
                .deletedByReceiver(deletedByReceiver)
                .build();
    }
}
