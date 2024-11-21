package com.example.demo0810.controller;

import com.example.demo0810.jwt.JwtUtill;
import com.example.demo0810.repository.etc.RefreshRepository;
import com.example.demo0810.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReissueController {

    /*
        refresh 토큰으로 access 토큰을 재발급 받기 위한 Controller임

        refresh 토큰으로 access 토큰을 재발급하고 refresh 토큰도 같이 재발급
        여기서, 재발급 받기 전 refresh 토큰을 사용 금지하게 처리해야 함
    */

    private final JwtUtill jwtUtill;
    private final RefreshRepository refreshRepository;
    private final UserService userService;

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {

        String refresh = null;

        Cookie[] cookies = request.getCookies();

        for (Cookie c : cookies) {
            if (c.getName().equals("refresh")) {
                refresh = c.getValue();
            }
        }

        if (refresh == null) {
            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
        }

        try {
            jwtUtill.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            return new ResponseEntity<>("access token expired", HttpStatus.BAD_REQUEST);
        }

        String category = jwtUtill.getCategory(refresh);

        if (!category.equals("refresh")) {
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        Boolean isExist = refreshRepository.existsByRefresh(refresh);

        if (!isExist) {
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        String username = jwtUtill.getUsername(refresh);
        String role = jwtUtill.getRole(refresh);
        String name = jwtUtill.getName(refresh);
        String email = jwtUtill.getEmail(refresh);
        String password = jwtUtill.getPassword(refresh);
        String gender = jwtUtill.getGender(refresh);
        String age = jwtUtill.getAge(refresh);

        String newAccess = jwtUtill.createJwtToken("access", username, role,600000L, name, email, password, gender, age);
        String newRefresh = jwtUtill.createJwtToken("refresh", username, role, 86400000L, name, email, password, gender, age);

        refreshRepository.deleteByRefresh(refresh);

        userService.addRefreshEntity(username, newRefresh, 86400000L);

        response.setHeader("access", newAccess);
        response.addCookie(userService.createCookie("refresh", newRefresh));

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
