package com.matan.blog.blog.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;

@Data
@Document(value = "RefreshToken")
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken {
    @MongoId(value = FieldType.OBJECT_ID)
    private String _id;
    private String token;
    private LocalDateTime createdDate;
}
