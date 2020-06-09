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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostService {
    private final PostMapper postMapper;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public void save(PostRequest postRequest) {
        User user = userRepository.findByEmail(postRequest.getUserEmail()).orElseThrow(()->new UserNotFoundException("No user Found with email : " + postRequest.getUserEmail()));

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
    public List<PostResponse> getAllPosts() {
        //TODO: add path variable in the post controller
        return postRepository.findAll(PageRequest.of(0, 20)).stream().map(postMapper::mapToResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PostResponse getPost(String id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException("post not fined whit that id: " + id));
        return postMapper.mapToResponse(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsByUsername(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("No user Found with email : " + email));
        if(user.getPosts() != null)
            return user.getPosts().stream().map(postMapper::mapToResponse).collect(Collectors.toList());
        return null;
    }
}
