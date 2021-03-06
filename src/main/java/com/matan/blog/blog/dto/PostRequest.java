package com.matan.blog.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostRequest {

    @NotBlank
    private String postTitle;
    @NotBlank
    private String description;
    @NotBlank
    private String createdDate;
}
