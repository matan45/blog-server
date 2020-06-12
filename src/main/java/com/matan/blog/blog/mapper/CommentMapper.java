package com.matan.blog.blog.mapper;

import com.matan.blog.blog.dto.CommentsRequest;
import com.matan.blog.blog.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(target = "_id", ignore = true)
    @Mapping(target = "createdDate", expression = "java(commentsRequest.getCreateDate())")
    @Mapping(target = "text", expression = "java(commentsRequest.getText())")
    @Mapping(target = "userName", expression = "java(commentsRequest.getUserName())")
    Comment map(CommentsRequest commentsRequest);
}
