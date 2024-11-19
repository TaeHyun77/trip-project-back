package com.example.demo0810.controller;


import com.example.demo0810.Entity.user.UserEntity;
import com.example.demo0810.dto.MessageDto;
import com.example.demo0810.exception.CustomException;
import com.example.demo0810.exception.ErrorCode;
import com.example.demo0810.jwt.JwtUtill;
import com.example.demo0810.repository.user.UserRepository;
import com.example.demo0810.service.MessageService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/message")
public class MessageController {

    private final UserRepository userRepository;
    private final MessageService messageService;
    private final JwtUtill jwtUtill;

    @PostMapping("/writeMessage")
    public void writeMessage(@RequestBody MessageDto messageDto) {

        UserEntity senderUser;
        UserEntity receiverUser;

        try {
            senderUser = userRepository.findByName(messageDto.getSenderName());
            receiverUser = userRepository.findByName(messageDto.getReceiverName());
        } catch (CustomException e) {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_USER);
        }

        messageDto.setSenderName(senderUser.getName());
        messageDto.setReceiverName(receiverUser.getName());

        messageService.writeMessage(messageDto);
    }

    @GetMapping("/receiveMessage")
    public ResponseEntity<List<MessageDto>> getReceivedMessage(HttpServletRequest request) {

        String authorizationHeader = request.getHeader("Authorization");

        String token = authorizationHeader.substring(7);

        String name = jwtUtill.getName(token);

        List<MessageDto> sentMessages = messageService.receivedMessage(name);

        return ResponseEntity.ok(sentMessages);
    }

    @GetMapping("/sendMessage")
    public ResponseEntity<List<MessageDto>> getSendedMessage(HttpServletRequest request) {

        String authorizationHeader = request.getHeader("Authorization");

        String token = authorizationHeader.substring(7);

        String name = jwtUtill.getName(token);

        List<MessageDto> sentMessages = messageService.sendedMessage(name);
        return ResponseEntity.ok(sentMessages);
    }

    // 보낸, 받은 쪽지 모두 삭제될 경우 DB에서 삭제 시킴

    // 받은 쪽지 삭제
    @DeleteMapping("/received/delete/{messageId}")
    public void deleteReceivedMessage(@PathVariable("messageId") Long messageId, HttpServletRequest request) {

        String authorizationHeader = request.getHeader("Authorization");

        String token = authorizationHeader.substring(7);

        String name = jwtUtill.getName(token);

        messageService.deleteMessageByReceiver(messageId, name);
    }

    // 보낸 쪽지 삭제
    @DeleteMapping("/sended/delete/{messageId}")
    public void deleteSentMessage(@PathVariable("messageId") Long messageId, HttpServletRequest request) {

        String authorizationHeader = request.getHeader("Authorization");

        String token = authorizationHeader.substring(7);

        String name = jwtUtill.getName(token);

        messageService.deleteMessageBySender(messageId, name);
    }
}
