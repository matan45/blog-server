package com.matan.blog.blog.controller;

import com.matan.blog.blog.dto.EditPostRequest;
import com.matan.blog.blog.dto.PostRequest;
import com.matan.blog.blog.dto.UserResponse;
import com.matan.blog.blog.service.AuthService;
import com.matan.blog.blog.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/")
@AllArgsConstructor
public class UserController {
    private final PostService postService;
    private final AuthService authService;

    @PostMapping
    public ResponseEntity<Void> createPost(@RequestBody PostRequest postRequest) {
        postService.save(postRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("byUser/{id}")
    public ResponseEntity<Boolean> isCreatedByUser(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(postService.CreatedByUser(id));
    }

    @DeleteMapping("delete/post")
    public void DeletePost(@RequestParam("postId") String id) {
        postService.deletePostById(id);
    }

    @PutMapping("edit")
    public ResponseEntity<Void> EditPost(@RequestBody EditPostRequest editPostRequest) {
        postService.editPost(editPostRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("profile")
    public ResponseEntity<UserResponse> getUserData() {
        return ResponseEntity.status(HttpStatus.OK).body(authService.userDetails());
    }
}
