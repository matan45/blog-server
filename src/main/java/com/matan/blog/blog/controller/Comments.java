package com.matan.blog.blog.controller;

import com.matan.blog.blog.dto.CommentsRequest;
import com.matan.blog.blog.service.CommentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@AllArgsConstructor
@Slf4j
public class Comments {
    private final SimpMessagingTemplate template;
    private final CommentService commentService;

    @MessageMapping("/send/post")
    public void onReceivedComment(CommentsRequest commentsRequest){
        log.info(commentsRequest.toString());
        commentService.createComment(commentsRequest);
        template.convertAndSend("/post/"+commentsRequest.getPostId(), commentsRequest );
    }
}
