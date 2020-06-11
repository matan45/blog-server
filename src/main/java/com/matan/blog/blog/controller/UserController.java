package com.matan.blog.blog.controller;

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

    @GetMapping("byUser/{id}")
    public ResponseEntity<Boolean> isCreatedByUser(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(postService.CreatedByUser(id));
    }

    @DeleteMapping("delete/post")
    public void DeletePost(@RequestParam("postId") String id) {
        postService.deletePostById(id);
    }
}
