package com.matan.blog.blog.dto;

import com.matan.blog.blog.model.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
    @NotBlank
    private String PostId;
    @NotBlank
    private String userName;
    @NotBlank
    private String postTitle;
    @NotBlank
    private String description;
    @NotNull
    private LocalDateTime createdDate;
    @Nullable
    private LocalDateTime editDate;
    @Nullable
    private List<Comment> comments;
}
