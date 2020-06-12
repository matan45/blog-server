package com.matan.blog.blog.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Document(value = "Post")
public class Post {
    @MongoId(value = FieldType.OBJECT_ID)
    private String _id;

    private String userName;

    @NotBlank(message = "Post Title cannot be empty or Null")
    private String postTitle;

    @Nullable
    private String description;

    @NotNull
    private String createdDate;

    @Nullable
    private String editDate;

    @Nullable
    private List<Comment> comments;
}
