package com.matan.blog.blog.service;

import com.matan.blog.blog.dto.CommentsRequest;
import com.matan.blog.blog.exception.PostNotFoundException;
import com.matan.blog.blog.mapper.CommentMapper;
import com.matan.blog.blog.model.Comment;
import com.matan.blog.blog.model.Post;
import com.matan.blog.blog.repository.PostRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CommentService {
    private final PostRepository postRepository;
    private final CommentMapper commentMapper;

    @Transactional
    public void createComment(CommentsRequest commentsRequest) {
        Post post = postRepository.findById(commentsRequest.getPostId()).orElseThrow(() -> new PostNotFoundException("cant fined post whit that id: " + commentsRequest.getPostId()));
        Comment comment = commentMapper.map(commentsRequest);
        if (post.getComments() == null) {
            List<Comment> commentList = new ArrayList<>();
            commentList.add(comment);
            post.setComments(commentList);
        } else {
            post.getComments().add(comment);
        }
        postRepository.save(post);
    }
}
