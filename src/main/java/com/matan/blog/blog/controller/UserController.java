package com.matan.blog.blog.controller;

import com.matan.blog.blog.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/")
@AllArgsConstructor
public class UserController {
    private final PostService postService;

    @GetMapping("/byUser/{id}")
    public ResponseEntity<Boolean> isCreatedByUser(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(postService.CreatedByUser(id));
    }
}
