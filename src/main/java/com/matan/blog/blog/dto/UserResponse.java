package com.matan.blog.blog.dto;

import com.matan.blog.blog.model.Post;
import com.mongodb.lang.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    @NotBlank
    private String username;
    @NotBlank
    private String email;
    @NotBlank
    private String created;
    @NotBlank
    private List<String> authorities;
    @PositiveOrZero
    private int commentsNumber;
    @Nullable
    private List<PostResponse> posts;
}
