package com.matan.blog.blog.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
@Slf4j
public class MvcExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> userAlreadyExistsExceptionHandler(UserAlreadyExistsException ex) {
        return new ResponseEntity<>(ErrorCodes.userAlreadyExists + " " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> UserNotFoundExceptionHandler(UserNotFoundException ex) {
        return new ResponseEntity<>(ErrorCodes.userNotFound + " " + ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<String> PostNotFoundExceptionHandler(PostNotFoundException ex) {
        return new ResponseEntity<>(ErrorCodes.postNotFound + " " + ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<String> JwtExceptionHandler(JwtException ex) {
        return new ResponseEntity<>(ErrorCodes.jwt + " " + ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(JwtRefreshException.class)
    public ResponseEntity<String> JwtRefreshExceptionHandler(JwtRefreshException ex) {
        return new ResponseEntity<>(ErrorCodes.jwtRefresh + " " + ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(JwtExpiredException.class)
    public ResponseEntity<String> JwtExpiredExceptionHandler(JwtExpiredException ex) {
        return new ResponseEntity<>(ErrorCodes.jwtExpired + " " + ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BotException.class)
    public ResponseEntity<String> BotExpiredExceptionHandler(BotException ex) {
        return new ResponseEntity<>(ErrorCodes.botExpired + " " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
