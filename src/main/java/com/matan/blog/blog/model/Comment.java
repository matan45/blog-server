package com.matan.blog.blog.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(value = "Comment")
@ToString
public class Comment {
    @MongoId(value = FieldType.OBJECT_ID)
    private String _id;
    @NotEmpty
    private String text;
    @NotNull
    private String userName;
    @NotNull
    private String createdDate;
}
