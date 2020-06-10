package com.matan.blog.blog.service;

import com.matan.blog.blog.dto.PostRequest;
import com.matan.blog.blog.dto.PostResponse;
import com.matan.blog.blog.exception.PostNotFoundException;
import com.matan.blog.blog.exception.UserNotFoundException;
import com.matan.blog.blog.mapper.PostMapper;
import com.matan.blog.blog.model.Post;
import com.matan.blog.blog.model.User;
import com.matan.blog.blog.repository.PostRepository;
import com.matan.blog.blog.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostService {
    private final PostMapper postMapper;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public void save(PostRequest postRequest) {
        User user = userRepository.findByEmail(postRequest.getUserEmail()).orElseThrow(() -> new UserNotFoundException("No user Found with email : " + postRequest.getUserEmail()));

        Post post = postMapper.map(postRequest);
        post.setUserName(user.getUsername());
        postRepository.save(post);
        if (user.getPosts() == null) {
            List<Post> postList = new ArrayList<>();
            postList.add(post);
            user.setPosts(postList);
        } else {
            user.getPosts().add(post);
        }
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts(int page) {
        return postRepository.findAll(PageRequest.of(page, 20)).stream().map(postMapper::mapToResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PostResponse getPost(String id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException("post not fined whit that id: " + id));
        return postMapper.mapToResponse(post);
    }

    @Transactional(readOnly = true)
    public User getCurrentUser() {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        return userRepository.findByEmail(principal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + principal.getUsername()));
    }

    public Boolean CreatedByUser(String id) {
        User currentUser = getCurrentUser();
        if (currentUser.getPosts() == null || currentUser.getPosts().size() == 0) {
            return false;
        }
        for (Post temp : currentUser.getPosts()) {
            if (temp.get_id().equals(id))
                return true;
        }
        return false;
    }
}
