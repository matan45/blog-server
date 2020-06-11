package com.matan.blog.blog.controller;

import com.matan.blog.blog.dto.PostRequest;
import com.matan.blog.blog.dto.PostResponse;
import com.matan.blog.blog.service.PostService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts/")
@AllArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<Void> createPost(@RequestBody PostRequest postRequest) {
        postService.save(postRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("query/all/{page}")
    public ResponseEntity<List<PostResponse>> getAllPosts(@PathVariable("page") int page) {
        return ResponseEntity.status(HttpStatus.OK).body(postService.getAllPosts(page));
    }

    @GetMapping("{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(postService.getPost(id));
    }
//TODO: PUT  post
}
