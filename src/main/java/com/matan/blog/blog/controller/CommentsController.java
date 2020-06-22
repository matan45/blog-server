package com.matan.blog.blog.controller;

import com.matan.blog.blog.dto.CommentsRequest;
import com.matan.blog.blog.service.CommentService;
import com.matan.blog.blog.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments/")
@AllArgsConstructor
public class CommentsController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Void> createComment(@RequestBody CommentsRequest commentsRequest) {
        commentService.createComment(commentsRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
//TODO: comment whit web socket
}
