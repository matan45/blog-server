package com.matan.blog.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditPostRequest {
    @NotNull
    private String editDate;
    @NotBlank
    private String postId;
    @NotBlank
    private String postTitle;
    @NotBlank
    private String description;
}
