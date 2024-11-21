package com.example.demo0810.service;

import com.example.demo0810.Entity.post.CommentEntity;
import com.example.demo0810.Entity.post.PostEntity;
import com.example.demo0810.Entity.user.UserEntity;
import com.example.demo0810.dto.comment.CommentRequestDto;
import com.example.demo0810.dto.comment.CommentUpdateDto;
import com.example.demo0810.exception.CustomException;
import com.example.demo0810.exception.ErrorCode;
import com.example.demo0810.jwt.JwtUtill;
import com.example.demo0810.repository.post.CommentRepository;
import com.example.demo0810.repository.post.PostRepository;
import com.example.demo0810.repository.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final JwtUtill jwtUtil;

    // 댓글 작성
    @Transactional
    public void CommentSave(Long postId, CommentRequestDto commentRequestDto, HttpServletRequest request) {

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, ErrorCode.UNATHORIZATION);
        }

        // 토큰에서 username 추출
        String token = authorizationHeader.substring(7);
        String username;

        try {
            username = jwtUtil.getUsername(token);
        } catch (Exception e) {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_USER);
        }

        // username을 통해 사용자 정보 조회
        UserEntity user = userRepository.findByUsername(username);
        if (user == null) {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_USER);
        }

        // postId를 통해 게시글 정보 조회
        PostEntity postEntity = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_POST));

        // 댓글 생성
        CommentEntity commentEntity = commentRequestDto.toComment();
        commentEntity.setUser(user);
        commentEntity.setPost(postEntity);
        commentEntity.setAuthor(user.getName());

        commentRepository.save(commentEntity);
    }


    // 댓글 수정
    @Transactional
    public void updateComment(Long id, CommentUpdateDto commentUpdateDto) {

        Optional<CommentEntity> comment = commentRepository.findById(id);

        CommentEntity commentUpdate = comment.orElseThrow(() ->
                new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_USER));

        commentUpdate.CommentUpdate(commentUpdateDto.getComment());

        commentRepository.save(commentUpdate);
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long id) {

        Optional<CommentEntity> comment = commentRepository.findById(id);

        try {
            comment.ifPresent(commentRepository::delete);
        } catch (Exception e) {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_USER);
        }
    }

    // 댓글 리스트
    public List<CommentEntity> getAllComment(Long postId) {
        List<CommentEntity> comments = commentRepository.findByPostId(postId);

        if (comments.isEmpty()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_POST);
        }

        return comments;
    }
}
