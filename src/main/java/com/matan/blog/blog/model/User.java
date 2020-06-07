package com.matan.blog.blog.model;

import com.mongodb.lang.Nullable;
import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Document(value = "User")
public class User {
    @MongoId(value = FieldType.OBJECT_ID)
    private String _id;

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    @Email
    @Indexed(unique = true)
    @NotEmpty(message = "Email is required")
    private String email;

    private LocalDateTime created;

    private List<String> authorities;

    @Nullable
    private List<Post> posts;

}
