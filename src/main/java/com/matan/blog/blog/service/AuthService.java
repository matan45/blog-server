package com.matan.blog.blog.service;

import com.matan.blog.blog.dto.*;
import com.matan.blog.blog.exception.BotException;
import com.matan.blog.blog.exception.UserAlreadyExistsException;
import com.matan.blog.blog.exception.UserNotFoundException;
import com.matan.blog.blog.mapper.UserMapper;
import com.matan.blog.blog.model.User;
import com.matan.blog.blog.repository.UserRepository;
import com.matan.blog.blog.security.JwtProvider;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;
    private final WebClient builder = WebClient.create();
    private final Environment env;

    @Transactional
    public void signUp(RegisterRequest registerRequest) {

        if(!registerRequest.getToken().isBlank())
            recaptcha(registerRequest.getToken());
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent())
            throw new UserAlreadyExistsException("there is already user whit that email");

        User user = userMapper.map(registerRequest);
        List<String> authorities = new ArrayList<>();
        authorities.add("USER");
        user.setAuthorities(authorities);
        userRepository.save(user);
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {

        if(!loginRequest.getToken().isBlank())
            recaptcha(loginRequest.getToken());
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new UserNotFoundException("No user Found with email : " + loginRequest.getEmail()));
        String Token = jwtProvider.generateToken(authenticate);
        return AuthenticationResponse.builder().authenticationToken(Token)
                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis())).username(user.getUsername())
                .email(loginRequest.getEmail()).build();
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
        String token = jwtProvider.generateTokenWithEmail(refreshTokenRequest.getEmail());
        return AuthenticationResponse.builder().authenticationToken(token)
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .email(refreshTokenRequest.getEmail()).build();
    }

    public AuthenticationResponse editUser(RefreshTokenRequest refreshTokenRequest, String username) {
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
        String token = jwtProvider.generateTokenWithEmail(refreshTokenRequest.getEmail());
        return AuthenticationResponse.builder().authenticationToken(token)
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis())).username(username)
                .email(refreshTokenRequest.getEmail()).build();
    }

    @Transactional(readOnly = true)
    public User getCurrentUser() {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        return userRepository.findByEmail(principal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + principal.getUsername()));
    }

    public void recaptcha(String token) {
        String secretKey=env.getProperty("recaptcha.key");
        String recaptchaUrl = "https://www.google.com/recaptcha/api/siteverify" +
                "?secret=" +
                secretKey +
                "&response=" +
                token;
            log.info(recaptchaUrl);
        ResponseRecap data = builder.method(HttpMethod.POST)
                .uri(URI.create(recaptchaUrl))
                .accept(MediaType.APPLICATION_FORM_URLENCODED).retrieve().bodyToMono(ResponseRecap.class).block();
        assert data != null;
        if (!(data.isSuccess() && data.getScore() > 0.6))
            throw new BotException("YOU ARE A BOT");

    }

}
