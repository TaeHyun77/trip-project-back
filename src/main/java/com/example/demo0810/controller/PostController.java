package com.example.demo0810.controller;

import com.example.demo0810.Entity.post.PostEntity;
import com.example.demo0810.dto.post.PostRequestDto;
import com.example.demo0810.dto.post.PostResponseDto;
import com.example.demo0810.dto.post.PostFreeUpdateDto;
import com.example.demo0810.dto.post.PostUpdateDto;
import com.example.demo0810.exception.CustomException;
import com.example.demo0810.exception.ErrorCode;
import com.example.demo0810.jwt.JwtUtill;
import com.example.demo0810.repository.post.PostRepository;
import com.example.demo0810.repository.user.UserRepository;
import com.example.demo0810.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequestMapping("/post")
@RequiredArgsConstructor
@Data
@RestController
public class PostController {

    private final PostService postService;
    private final PostRepository postRepository;
    private final JwtUtill jwtUtill;
    private final UserRepository userRepository;

    // 게시글 작성
    @PostMapping("/write")
    public void postWrite(@RequestPart("postData") PostRequestDto postRequestDto, HttpServletRequest request, @RequestPart("file") MultipartFile file) {

        postService.PostSave(postRequestDto, request, file);

    }

    // 자유 게시글 수정
    @PostMapping("/free/update/{postId}")
    public void updateFreePost(@PathVariable("postId") Long postId, @RequestPart("postUpdateData") PostFreeUpdateDto postFreeUpdateDto, @RequestPart("file") MultipartFile file) {

        postService.updateFreePost(postId, postFreeUpdateDto, file);

    }

    // 동행 게시글 수정
    @PostMapping("/update/{postId}")
    public void updatePost(@PathVariable("postId") Long postId, @RequestPart("postUpdateData") PostUpdateDto postUpdateDto, @RequestPart("file") MultipartFile file) {

        postService.updatePost(postId, postUpdateDto, file);

    }

    // 게시글 삭제
    @DeleteMapping("/delete/{id}")
    public void deletePost(@PathVariable("id") Long id) {

        postService.deletePost(id);

    }

    // 게시글 상세 정보
    @GetMapping("/info/{id}")
    public PostResponseDto PostInfo(@PathVariable("id") Long id) {

        Optional<PostEntity> post = postRepository.findById(id);

        if (post.isPresent()) {
            PostEntity postEntity = post.get();

            postRepository.updateCount(id);

            List<String> hashtags = postEntity.getPostTagMaps().stream()
                    .map(postTagMap -> postTagMap.getTag().getTagContent())
                    .collect(Collectors.toList());

            List<String> participation = postEntity.getPostPartiMap().stream()
                    .map(postPartiMap -> postPartiMap.getParticipation().getParticipationName())
                    .collect(Collectors.toList());

            String defaultImagePath = "C:\\Users\\PARK TH\\AppData\\Local\\Temp\\tomcat.8080.17598021890391925618\\work\\Tomcat\\localhost\\ROOT\\profileImages\\basic.png";

            // 프로필 이미지가 없으면 기본 경로로 설정
            String postImageUrl = (postEntity.getPostImage() != null && postEntity.getPostImage().getPostImageUrl() != null)
                    ? postEntity.getPostImage().getPostImageUrl()
                    : defaultImagePath;

            return new PostResponseDto(postEntity.getId(), postEntity.getTitle(), postEntity.getContent(), postEntity.getWriter(), postEntity.getCount()
                    , postEntity.getCreatedDate(), postEntity.getUpdatedDate(), postEntity.getMbti(), postEntity.getPlace(), postEntity.getStartDate(), postEntity.getEndDate(), hashtags, postImageUrl, postEntity.getPeople(), postEntity.getPostCategory(), participation, postEntity.isStatus());
        } else {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_POST);
        }
    }

    // 게시물 리스트
    @GetMapping("/postList")
    public List<PostEntity> getPostList() {

        return postService.getAllPost();

    }

    @Transactional
    @PostMapping("/{id}/{user_name}/participate")
    public void participate(@PathVariable("id") Long id, @PathVariable("user_name") String user_name) {

        postService.Participation(id, user_name);
    }

    @Transactional
    @DeleteMapping("/delete/participate/{id}/{user_name}")
    public void participationCancel(@PathVariable("id") Long id,@PathVariable("user_name") String user_name) {

        postService.participationCancel(id, user_name);
    }

    @PostMapping("/update/{id}/status")
    public void updatePostStatus(@PathVariable("id") Long id) {
        postService.updatePostStatus(id);
    }
}
