package com.example.demo0810.controller;

import com.example.demo0810.Entity.user.UserEntity;
import com.example.demo0810.dto.user.UserRequestDto;
import com.example.demo0810.dto.user.UserResponseDto;
import com.example.demo0810.dto.user.UserUpdateDto;
import com.example.demo0810.exception.CustomException;
import com.example.demo0810.exception.ErrorCode;
import com.example.demo0810.jwt.JwtUtill;
import com.example.demo0810.repository.user.UserRepository;
import com.example.demo0810.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtUtill jwtUtill;

    @GetMapping("/")
    public String main() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iter = authorities.iterator();
        GrantedAuthority auth = iter.next();
        String role = auth.getAuthority();

        return "main Controller" + " - " + username + " - " + role;
    }

    // 회원 가입
    @PostMapping("/join")
    public void joinProcess(@RequestPart("user") UserRequestDto userRequestDto,
                            @RequestPart("file") MultipartFile file) {

        userService.joinP(userRequestDto, file);

    }

    // 사용자 정보 조회
    @Secured("ROLE_USER")
    @GetMapping("/info")
    public ResponseEntity<?> userInfo(HttpServletRequest request) {
        // 요청의 Authorization 헤더에서 JWT 토큰 추출
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return new ResponseEntity<>("UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
        }

        // "Bearer " 제거하여 순수 JWT 토큰만 추출
        String token = authorizationHeader.substring(7);

        try {
            String username = jwtUtill.getUsername(token);
            String role = jwtUtill.getRole(token);
            String category = jwtUtill.getCategory(token);

            UserEntity user = userRepository.findByUsername(username);
            String name = user.getName();
            String email = user.getEmail();
            String gender = user.getGender();
            String age = user.getAge();
            String selfIntro = user.getSelfIntro();

            // 기본 이미지 경로 설정
            String defaultImagePath = "C:\\Users\\PARK TH\\AppData\\Local\\Temp\\tomcat.8080.17598021890391925618\\work\\Tomcat\\localhost\\ROOT\\profileImages\\basic.png";

            // 프로필 이미지가 없으면 기본 경로로 설정
            String profileImageUrl = (user.getImage() != null && user.getImage().getProfileImageUrl() != null)
                    ? user.getImage().getProfileImageUrl()
                    : defaultImagePath;

            List<String> follow = user.getUserFollowMap().stream()
                    .map(userFollowMap -> userFollowMap.getFollow().getFollowName())
                    .collect(Collectors.toList());

            // UserResponseDto 객체 생성
            UserResponseDto userInfoResponse = new UserResponseDto(
                    username,
                    role,
                    category,
                    name,
                    email,
                    user.getCreatedDate(),
                    profileImageUrl,
                    gender,
                    age,
                    selfIntro,
                    follow
            );

            System.out.println("User info response: " + userInfoResponse);


            return new ResponseEntity<>(userInfoResponse, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/postWriterInfo/{name}")
    public ResponseEntity<?> postWriterInfo(@PathVariable("name") String name) {

        UserEntity user;

        try {
            user = userRepository.findByName(name);
        } catch (CustomException e) {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_USER);
        }

        String username = user.getUsername();
        String role = user.getRole();
        String category = user.getCategory();
        String email = user.getEmail();
        String gender = user.getGender();
        String age = user.getAge();
        String selfIntro = user.getSelfIntro();

        String defaultImagePath = "C:\\Users\\PARK TH\\AppData\\Local\\Temp\\tomcat.8080.17598021890391925618\\work\\Tomcat\\localhost\\ROOT\\profileImages\\basic.png";

        String profileImageUrl = (user.getImage() != null && user.getImage().getProfileImageUrl() != null)
                ? user.getImage().getProfileImageUrl()
                : defaultImagePath;

        List<String> follow = user.getUserFollowMap().stream()
                .map(userFollowMap -> userFollowMap.getFollow().getFollowName())
                .collect(Collectors.toList());

        UserResponseDto userInfoResponse = new UserResponseDto(
                username,
                role,
                category,
                name,
                email,
                user.getCreatedDate(),
                profileImageUrl,
                gender,
                age,
                selfIntro,
                follow
        );

        return new ResponseEntity<>(userInfoResponse, HttpStatus.OK);
    }


    // 사용자 정보 수정
    @PostMapping("/update")
    public void updateUser(HttpServletRequest request, @RequestBody UserUpdateDto userUpdateDto) {

        userService.updateUser(userUpdateDto, request);

    }

    // 사용자 정보 삭제
    @DeleteMapping("/delete/{username}")
    public void deleteUser(@PathVariable("username") String username) {

        System.out.println("Received username: " + username);

        userService.deleteUser(username);

    }

    // 유저 리스트
    @GetMapping("/userList")
    public List<UserEntity> getUserList() {
        return userService.getAllUser();
    }

    @Transactional
    @PostMapping("/{name}/follow")
    public void follow(@PathVariable("name") String name, HttpServletRequest request) {

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, ErrorCode.UNATHORIZATION);
        }

        String token = authorizationHeader.substring(7);

        String username = jwtUtill.getUsername(token);

        userService.follow(name, username);
    }

    @Transactional
    @DeleteMapping("/{name}/delete/follow")
    public void followCancel(@PathVariable("name") String name, HttpServletRequest request) {

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, ErrorCode.UNATHORIZATION);
        }

        String token = authorizationHeader.substring(7);

        String username = jwtUtill.getUsername(token);

        userService.followCancel(name, username);
    }
}


