package com.matan.blog.blog.exception;

public class JwtException extends RuntimeException {
    public JwtException(String message) {
        super(message);
    }
}
