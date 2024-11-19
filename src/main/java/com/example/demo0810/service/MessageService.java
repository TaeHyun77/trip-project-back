package com.example.demo0810.service;


import com.example.demo0810.Entity.MessageEntity;
import com.example.demo0810.Entity.user.UserEntity;
import com.example.demo0810.dto.MessageDto;
import com.example.demo0810.exception.CustomException;
import com.example.demo0810.exception.ErrorCode;
import com.example.demo0810.repository.MessageRepository;
import com.example.demo0810.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Transactional
    public void writeMessage(MessageDto messageDto) {

        UserEntity receiver = userRepository.findByName(messageDto.getReceiverName());
        UserEntity sender = userRepository.findByName(messageDto.getSenderName());

        MessageEntity message = messageDto.toEntity(sender, receiver);

        messageRepository.save(message);
    }

    // 받은 쪽지함
    @Transactional
    public List<MessageDto> receivedMessage(String name) {

        UserEntity user = userRepository.findByName(name);

        List<MessageEntity> messages = messageRepository.findAllByReceiver(user);

        List<MessageEntity> messageList = new ArrayList<>();

        return messages.stream()
                .filter(message -> !message.isDeletedByReceiver())
                .map(message -> MessageDto.builder()
                        .id(message.getId())
                        .title(message.getTitle())
                        .content(message.getContent())
                        .senderName(message.getSender().getName())
                        .receiverName(message.getReceiver().getName())
                        .deletedBySender(message.isDeletedBySender())
                        .deletedByReceiver(message.isDeletedByReceiver())
                        .createdDate(message.getCreatedDate())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public List<MessageDto> sendedMessage(String name) {

        UserEntity user = userRepository.findByName(name);

        List<MessageEntity> messages = messageRepository.findAllBySender(user);

        List<MessageEntity> messageList = new ArrayList<>();

        return messages.stream()
                .filter(message -> !message.isDeletedBySender())
                .map(message -> MessageDto.builder()
                        .id(message.getId())
                        .title(message.getTitle())
                        .content(message.getContent())
                        .senderName(message.getSender().getName())
                        .receiverName(message.getReceiver().getName())
                        .deletedBySender(message.isDeletedBySender())
                        .deletedByReceiver(message.isDeletedByReceiver())
                        .createdDate(message.getCreatedDate())
                        .build())
                .collect(Collectors.toList());
    }

    // 받은 쪽지 삭제
    @Transactional
    public Object deleteMessageByReceiver(Long messageId, String name) {

        MessageEntity message = messageRepository.findById(messageId).orElseThrow(() -> {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_MESSAGE);
        });

        UserEntity user;

        try {
            user = userRepository.findByName(name);
        } catch (CustomException e) {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_USER);
        }

        // 현재 로그인 한 사용자 이름 || 쪽지 받은 사람의 이름

        if (name.equals(message.getReceiver().getName())) {

            message.deleteByReceiver(); // 받은 사람은 삭제

            if (message.isDeleted()) { // 수신 & 송신자 모두 해당 쪽지를 삭제할 시

                messageRepository.delete(message);
                return "양쪽 모두 삭제";
            }

            return "한쪽만 삭제";

        } else {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.INCONSISTENT_USER);
        }
    }


    @Transactional
    public Object deleteMessageBySender(Long messageId, String name) {

        MessageEntity message = messageRepository.findById(messageId).orElseThrow(() -> {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_MESSAGE);
        });

        UserEntity user;

        try {
            user = userRepository.findByName(name);
        } catch (CustomException e) {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_USER);
        }

        if(user == message.getSender()) {
            message.deleteBySender(); // 보낸 사람은 삭제 ( true )

            if (message.isDeleted()) {
                messageRepository.delete(message);
                return "양쪽 모두 삭제";
            }

            return "한쪽만 삭제";
        } else {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.INCONSISTENT_USER);
        }
    }
}
