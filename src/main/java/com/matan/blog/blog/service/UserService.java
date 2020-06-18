package com.matan.blog.blog.service;

import com.matan.blog.blog.dto.*;
import com.matan.blog.blog.mapper.UserMapper;
import com.matan.blog.blog.model.Post;
import com.matan.blog.blog.model.User;
import com.matan.blog.blog.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@AllArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    private final AuthService authService;
    private final PostService postService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse userDetails() {
        User user = authService.getCurrentUser();
        if (user.getPosts() == null)
            user.setPosts(new ArrayList<>());
        return userMapper.mapToResponse(user);
    }

    public Boolean CreatedByUser(String id) {
        User currentUser = authService.getCurrentUser();
        if (currentUser.getPosts() != null && currentUser.getPosts().size() > 0) {
            for (Post temp : currentUser.getPosts()) {
                if (temp.get_id().equals(id))
                    return true;
            }
        }
        return false;
    }

    public void deletePostById(String id) {
        postService.deletePostById(id);
    }

    public void editPost(EditPostRequest editPostRequest) {
        postService.editPost(editPostRequest);
    }

    public void save(PostRequest postRequest) {
        postService.save(postRequest);
    }

    public void deleteUser() {
        userRepository.deleteById(authService.getCurrentUser().get_id());
    }

    public AuthenticationResponse EditUser(EditUser editUser) {
        User user = authService.getCurrentUser();
        if (!editUser.getEmail().equals(user.getEmail()))
            user.setEmail(editUser.getEmail());
        if (!editUser.getUsername().equals(user.getUsername()))
            user.setUsername(editUser.getUsername());
        if (!editUser.getPassword().isBlank())
            user.setPassword(passwordEncoder.encode(editUser.getPassword()));

        userRepository.save(user);

        return authService.editUser(new RefreshTokenRequest(editUser.getRefreshToken(),user.getEmail()),user.getUsername());
    }
}
