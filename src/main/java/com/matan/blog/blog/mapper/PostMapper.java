package com.matan.blog.blog.mapper;

import com.matan.blog.blog.dto.EditPostRequest;
import com.matan.blog.blog.dto.PostRequest;
import com.matan.blog.blog.dto.PostResponse;
import com.matan.blog.blog.model.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface PostMapper {
    @Mapping(target = "_id", ignore = true)
    @Mapping(target = "createdDate", expression = "java(postDto.getCreatedDate())")
    @Mapping(target = "postTitle", expression = "java(postDto.getPostTitle())")
    @Mapping(target = "description", expression = "java(postDto.getDescription())")
    Post map(PostRequest postDto);

    @Mapping(target = "postId", expression ="java(post.get_id())")
    @Mapping(target = "userName", expression = "java(post.getUserName())")
    @Mapping(target = "postTitle", expression = "java(post.getPostTitle())")
    @Mapping(target = "description", expression = "java(post.getDescription())")
    @Mapping(target = "createdDate", expression = "java(post.getCreatedDate())")
    @Mapping(target = "editDate", expression = "java(post.getEditDate())")
    @Mapping(target = "comments", expression = "java(post.getComments())")
    PostResponse mapToResponse(Post post);

    @Mapping(target = "_id", expression = "java(postDto.getPostId())")
    @Mapping(target = "editDate", expression = "java(postDto.getEditDate())")
    @Mapping(target = "postTitle", expression = "java(postDto.getPostTitle())")
    @Mapping(target = "description", expression = "java(postDto.getDescription())")
    @Mapping(target = "comments", expression = "java(oldPost.getComments())")
    Post mapEdit(EditPostRequest postDto,Post oldPost);

}
