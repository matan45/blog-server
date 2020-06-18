package com.matan.blog.blog.controller;

import com.matan.blog.blog.dto.*;
import com.matan.blog.blog.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<Void> createPost(@RequestBody PostRequest postRequest) {
        userService.save(postRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("byUser/{id}")
    public ResponseEntity<Boolean> isCreatedByUser(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.CreatedByUser(id));
    }

    @DeleteMapping("delete/post")
    public void DeletePost(@RequestParam("postId") String id) {
        userService.deletePostById(id);
    }

    @PutMapping("edit")
    public ResponseEntity<Void> EditPost(@RequestBody EditPostRequest editPostRequest) {
        userService.editPost(editPostRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("profile")
    public ResponseEntity<UserResponse> getUserData() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.userDetails());
    }

    @PutMapping("profile/edit")
    public ResponseEntity<AuthenticationResponse> editUser(@RequestBody EditUser editUser) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.EditUser(editUser));
    }

    @DeleteMapping("delete")
    public ResponseEntity<String> deleteUser() {
        userService.deleteUser();
        return ResponseEntity.status(HttpStatus.OK).body("User Deleted Successfully!!");
    }
}
