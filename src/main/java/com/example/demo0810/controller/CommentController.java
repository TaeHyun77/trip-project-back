package com.example.demo0810.controller;

import com.example.demo0810.Entity.post.CommentEntity;
import com.example.demo0810.dto.comment.CommentRequestDto;
import com.example.demo0810.dto.comment.CommentUpdateDto;
import com.example.demo0810.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    // 댓글 작성
    @PostMapping("/{id}/write")
    public void commentWrite(@PathVariable("id") Long id, @RequestBody CommentRequestDto commentRequestDto, HttpServletRequest request) {

        commentService.CommentSave(id, commentRequestDto, request);

    }

    // 댓글 수정
    @PostMapping("/{id}/update")
    public void updateComment(@PathVariable("id") Long id, @RequestBody CommentUpdateDto commentUpdateDto) {

        commentService.updateComment(id, commentUpdateDto);

    }

    // 댓글 삭제
    @DeleteMapping("/delete/{id}")
    public void deleteComment(@PathVariable("id") Long id, HttpServletRequest request) {

        commentService.deleteComment(id);

    }

    // 특정 게시물 ID로 댓글 목록 조회
    @GetMapping("/commentList/{id}")
    public List<CommentEntity> getCommentList(@PathVariable("id") Long id) {
        return commentService.getAllComment(id);
    }
}