package com.example.demo0810.service;

import com.example.demo0810.Entity.etc.RefreshEntity;
import com.example.demo0810.Entity.user.follow.Follow;
import com.example.demo0810.Entity.user.UserEntity;
import com.example.demo0810.Entity.user.follow.UserFollowMap;
import com.example.demo0810.dto.user.UserRequestDto;
import com.example.demo0810.dto.user.UserUpdateDto;
import com.example.demo0810.exception.CustomException;
import com.example.demo0810.exception.ErrorCode;
import com.example.demo0810.jwt.JwtUtill;
import com.example.demo0810.repository.etc.RefreshRepository;
import com.example.demo0810.repository.user.FollowRepository;
import com.example.demo0810.repository.user.UserFollowMapRepository;
import com.example.demo0810.repository.user.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RefreshRepository refreshRepository;
    private final FollowRepository followRepository;
    private final UserFollowMapRepository userFollowMapRepository;
    private final JwtUtill jwtUtill;

    // 유저 등록
    @Transactional
    public void joinP(UserRequestDto userRequestDto, MultipartFile file) {

        // 비밀번호 암호화
        String encodedPassword = bCryptPasswordEncoder.encode(userRequestDto.getPassword());
        userRequestDto.setPassword(encodedPassword);

        Boolean isExist = userRepository.existsByUsername(userRequestDto.getUsername());

        if (isExist) {
            // 중복된 ID일 경우 CustomException 발생
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.DUPLICATED_ID_FAILED);
        }

        // 파일 저장 로직 추가
        if (file != null && !file.isEmpty()) {
            String imageUrl = saveFile(file);  // 파일을 저장하고 경로 반환
            userRequestDto.setProfileImageUrl(imageUrl); // 이미지 URL 저장
        }

        // 권한 설정
        if (!userRequestDto.getUsername().equals("admin0515")) {
            userRequestDto.setRole("ROLE_USER");
        } else {
            userRequestDto.setRole("ROLE_ADMIN");
        }

        // UserEntity로 변환 후 저장
        UserEntity userEntity = userRequestDto.toEntity();

        if (userEntity.getImage() != null) {
            userEntity.getImage().setUser(userEntity);  // ImageEntity와 UserEntity 간의 양방향 연관 관계 설정
        }

        userRepository.save(userEntity);
    }


    public String saveFile(MultipartFile file) {

        // 파일을 저장할 기본 경로
        String directoryPath = "C:/Image";

        // 디렉토리가 존재하지 않으면 생성
        File directory = new File(directoryPath);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        // 파일 이름 생성
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        // "C:/Image" -> 이 경로에 저장
        File saveFile = new File(directoryPath, fileName);

        try {
            file.transferTo(saveFile);
        } catch (IOException e) {
            System.out.println("파일 저장 중 오류 발생: " + e.getMessage());  // 로그에 구체적인 오류 출력
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.FILE_SAVE_FAILED, e);
        }

        // 저장된 파일의 경로 반환
        return "/profileImages/" + fileName;  // 정적 리소스의 URL 경로 반환
    }


    // 유저 수정
    @Transactional
    public void updateUser(UserUpdateDto userUpdateDto, HttpServletRequest request) {

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.UNATHORIZATION);
        }

        String token = authorizationHeader.substring(7);

        String username = jwtUtill.getUsername(token);

        UserEntity user;

        try {
            user = userRepository.findByUsername(username);
        } catch (CustomException e) {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_USER);
        }

        // 사용자 name과 email만 업데이트
        user.updateUser(userUpdateDto.getName(), userUpdateDto.getEmail(), userUpdateDto.getGender(), userUpdateDto.getAge(), userUpdateDto.getSelfIntro());

        // DB에 저장
        userRepository.save(user);
    }

    // 유저 삭제
    @Transactional
    public void deleteUser(String username) {

        try {
            UserEntity user = userRepository.findByUsername(username);

            userRepository.delete(user);
        } catch (Exception e) {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_USER);
        }
    }

    // 유저 리스트
    public List<UserEntity> getAllUser() {
        return userRepository.findAll();
    }

    public void follow(String name, String username) {

        UserEntity user;

        try {
            user = userRepository.findByUsername(username);
        } catch (CustomException e) {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_USER);
        }

        // 중복 팔로우 확인
        boolean alreadyFollow = user.getUserFollowMap().stream()
                .anyMatch(userFollowMap -> userFollowMap.getFollow().getFollowName().equals(name));

        if (alreadyFollow) {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.DUPLICATED_USER);
        }

        Follow follow = followRepository.findByFollowName(name)
                .orElseGet(() -> {
                    Follow newFollow = new Follow();
                    newFollow.setFollowName(name);
                    return followRepository.save(newFollow);
                });

        UserFollowMap userFollowMap = new UserFollowMap();
        userFollowMap.setUser(user);
        userFollowMap.setFollow(follow);

        userFollowMapRepository.save(userFollowMap);
    }

    @Transactional
    public void followCancel(String name, String username) {

        UserEntity user;

        try {
            user = userRepository.findByUsername(username);
        } catch (CustomException e) {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_USER);
        }

        UserFollowMap targetfollowMap = user.getUserFollowMap().stream()
                .filter(followMap -> followMap.getFollow().getFollowName().equals(name))
                .findFirst()
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ErrorCode.PARTICIPATION_FAILED));

        user.getUserFollowMap().remove(targetfollowMap);

        userFollowMapRepository.delete(targetfollowMap);
    }

    public void addRefreshEntity(String username, String refresh, Long expired) {
        Date date = new Date(System.currentTimeMillis() + expired);

        RefreshEntity refreshEntity = new RefreshEntity();
        refreshEntity.setUsername(username);
        refreshEntity.setRefresh(refresh);
        refreshEntity.setExpiration(date.toString());

        refreshRepository.save(refreshEntity);
    }

    public Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        cookie.setHttpOnly(true);

        return cookie;
    }



}
