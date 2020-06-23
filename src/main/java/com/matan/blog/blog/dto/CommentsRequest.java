package com.matan.blog.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentsRequest {
    @NotBlank
    private String postId;
    @Nullable
    private String text;
    @NotBlank
    private String userName;
    @NotNull
    private String createdDate;
    @NotBlank
    private String userEmail;
}
