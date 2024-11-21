package com.example.demo0810.config;

import com.example.demo0810.jwt.CustomLogoutFilter;
import com.example.demo0810.jwt.JwtFilter;
import com.example.demo0810.jwt.JwtUtill;
import com.example.demo0810.jwt.LoginFilter;
import com.example.demo0810.repository.etc.RefreshRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Collections;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity // Spring Security를 활성화하는 어노테이션
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration; // 인증 관리 설정을 제공하는 객체
    private final JwtUtill jwtUtill;
    private final RefreshRepository refreshRepository;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager (AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                .csrf(csrf -> csrf.disable())

                .formLogin(form -> form.disable())
                .httpBasic(httpBasic -> httpBasic.disable())

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/", "/user/**", "/post/**", "/comment/**", "/file/**", "/api/**", "/profileImages/**", "/google/**", "/message/**", "/Image/**").permitAll()
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .requestMatchers("/status", "/images/**", "/error/**", "/api/develop/**").permitAll()
                        .requestMatchers("/reissue").permitAll()
                        .anyRequest().authenticated())

                .addFilterBefore(new JwtFilter(jwtUtill), LoginFilter.class)
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtill, refreshRepository), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new CustomLogoutFilter(jwtUtill, refreshRepository), LogoutFilter.class)

                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() { // Security Filter Chain 내에서 CORS 정책을 적용

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
        configuration.setAllowedMethods(Collections.singletonList("*"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.setExposedHeaders(Collections.singletonList("Authorization"));
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
