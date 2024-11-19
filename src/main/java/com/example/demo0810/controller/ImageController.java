package com.example.demo0810.controller;

import com.example.demo0810.dto.image.ImageResponseDto;
import com.example.demo0810.dto.image.ImageUploadDto;
import com.example.demo0810.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000") // Allow CORS for this controller
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/upload")
    public void upload(ImageUploadDto imageUploadDto, @PathVariable("username") String username) {
        imageService.upload(imageUploadDto, username);
    }

    // 사용자 프로필 이미지 로드
    @CrossOrigin(origins = "http://localhost:3001")
    @GetMapping(value = "/{username}/image", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImageResponseDto> getUserImage(@PathVariable("username") String username) {
        return imageService.getImage(username);
    }

    @CrossOrigin(origins = "http://localhost:3001")
    @GetMapping(value = "/{name}/image/name", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImageResponseDto> getUserByNameImage(@PathVariable("name") String name) {
        System.out.println("사용자 이름: " + name);

        return imageService.getImageByName(name);
    }

    @CrossOrigin(origins = "http://localhost:3001")
    @GetMapping(value = "/{postId}/postImage", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImageResponseDto> getPostImage(@PathVariable("postId") Long postId) {
        return imageService.getPostImage(postId);
    }

    @PostMapping("/uploadProfileImage/{username}")
    public void updateUserImage(@PathVariable("username") String username,
                                             @RequestParam("file") MultipartFile file) {

        imageService.updateImage(username, file);

    }


}
