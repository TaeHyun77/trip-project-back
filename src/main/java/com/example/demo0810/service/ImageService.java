package com.example.demo0810.service;

import com.example.demo0810.Entity.etc.ImageEntity;
import com.example.demo0810.Entity.post.PostEntity;
import com.example.demo0810.Entity.post.PostImageEntity;
import com.example.demo0810.Entity.user.UserEntity;
import com.example.demo0810.dto.image.ImageResponseDto;
import com.example.demo0810.dto.image.ImageUploadDto;
import com.example.demo0810.exception.CustomException;
import com.example.demo0810.exception.ErrorCode;
import com.example.demo0810.repository.user.ImageRepository;
import com.example.demo0810.repository.post.PostImageRepository;
import com.example.demo0810.repository.post.PostRepository;
import com.example.demo0810.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ImageService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ImageRepository imageRepository;
    private final PostImageRepository postImageRepository;

    @Value("${file.path}")
    private String uploadFolder; // C://FileUpload

    public void upload(ImageUploadDto imageUploadDto, String username) {

        UserEntity user;

        try {
            user = userRepository.findByUsername(username);
        } catch (CustomException e){
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_USER);
        }

        MultipartFile file = imageUploadDto.getFile();

        // 저장할 이미지의 이름이 중복 되지 않게 하기 위함
        UUID uuid = UUID.randomUUID();

        // 저장할 이미지의 이름
        String imageFileName = uuid + "_" + file.getOriginalFilename();

        File destinationFile = new File(uploadFolder + "/" + imageFileName);

        try {
            file.transferTo(destinationFile);

            ImageEntity image = imageRepository.findByUser(user);

            if (image != null) { // 이미지가 이미 존재할 때
                image.updateUrl("/profileImages/" + imageFileName);
            } else { // 이미지가 존재하지 않을 때
                image = ImageEntity.builder()
                        .user(user)
                        .profileImageUrl("/profileImages/" + imageFileName)
                        .build();

                imageRepository.save(image);
            }

        } catch (CustomException e) {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.FILE_UPLOAD_FAILED);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 유저에 설정된 이미지 url 값을 가져오는 service
    public ResponseEntity<ImageResponseDto> getImage(String username) {

        UserEntity user;

        try {
            user = userRepository.findByUsername(username);
        } catch (CustomException e){
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_USER);
        }

        ImageEntity image = imageRepository.findByUser(user);

        // 아직 설정을 안했을 경우 기본 이미지 경로
        String defaultImageUrl = "/profileImages/basic.png";

        String imageUrl = (image != null) ? image.getProfileImageUrl() : defaultImageUrl;

        ImageResponseDto responseDto = ImageResponseDto.builder()
                .url(imageUrl)
                .build();

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(responseDto);
    }

    public ResponseEntity<ImageResponseDto> getImageByName(String name) {

        UserEntity user;

        try {
            user = userRepository.findByName(name);
        } catch (CustomException e){
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_USER);
        }

        ImageEntity image = imageRepository.findByUser(user);

        // 아직 설정을 안했을 경우 기본 이미지 경로
        String defaultImageUrl = "/profileImages/basic.png";

        String imageUrl = (image != null) ? image.getProfileImageUrl() : defaultImageUrl;

        ImageResponseDto responseDto = ImageResponseDto.builder()
                .url(imageUrl)
                .build();

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(responseDto);
    }

    // 유저에 설정된 이미지 url 값을 가져오는 service
    public ResponseEntity<ImageResponseDto> getPostImage(Long id) {

        Optional<PostEntity> post;

        try {
            post = postRepository.findById(id);
        } catch (CustomException e){
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_POST);
        }

        PostImageEntity postImage = postImageRepository.findByPost(post.get());

        // 아직 설정을 안했을 경우 기본 이미지 경로
        String defaultImageUrl = "/profileImages/basic.png";

        String imageUrl = (postImage != null) ? postImage.getPostImageUrl() : defaultImageUrl;

        ImageResponseDto responseDto = ImageResponseDto.builder()
                .url(imageUrl)
                .build();

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(responseDto);
    }

    public void updateImage(String username, MultipartFile file) {

        UserEntity user;

        try {
            user = userRepository.findByUsername(username);
        } catch (CustomException e) {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_USER);
        }

        UUID uuid = UUID.randomUUID();

        String imageFileName = uuid + "_" + file.getOriginalFilename();

        File destinationFile = new File("C:/Image/" + imageFileName);

        try {
            file.transferTo(destinationFile);

            ImageEntity image = imageRepository.findByUser(user);

            image.updateUrl("/profileImages/" + imageFileName);

            imageRepository.save(image);

        } catch (CustomException e) {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.FILE_UPLOAD_FAILED);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
