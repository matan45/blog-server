package com.matan.blog.blog.mapper;

import com.matan.blog.blog.dto.RegisterRequest;
import com.matan.blog.blog.dto.UserResponse;
import com.matan.blog.blog.model.User;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@NoArgsConstructor
@Mapper(componentModel = "spring")
public abstract class UserMapper {
    @Autowired
    protected final PasswordEncoder passwordEncoder = null;
    @Autowired
    protected final PostMapper postMapper = null;

    @Mapping(target = "_id", ignore = true)
    @Mapping(target = "created", expression = "java(registerRequest.getCreated())")
    @Mapping(target = "username", expression = "java(registerRequest.getUsername())")
    @Mapping(target = "email", expression = "java(registerRequest.getEmail())")
    @Mapping(target = "password", expression = "java(passwordEncoder.encode(registerRequest.getPassword()))")
    public abstract User map(RegisterRequest registerRequest);

    @Mapping(target = "created", expression = "java(user.getCreated())")
    @Mapping(target = "username", expression = "java(user.getUsername())")
    @Mapping(target = "email", expression = "java(user.getEmail())")
    @Mapping(target = "authorities", expression = "java(user.getAuthorities())")
    @Mapping(target = "commentsNumber", expression = "java(user.getCommentsNumber())")
    @Mapping(target = "posts", expression = "java(user.getPosts().stream().map(postMapper::mapToResponse).collect(java.util.stream.Collectors.toList()))")
    public abstract UserResponse mapToResponse(User user);
}
