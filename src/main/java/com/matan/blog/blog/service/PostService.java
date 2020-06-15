package com.matan.blog.blog.service;

import com.matan.blog.blog.dto.EditPostRequest;
import com.matan.blog.blog.dto.PostRequest;
import com.matan.blog.blog.dto.PostResponse;
import com.matan.blog.blog.exception.PostNotFoundException;
import com.matan.blog.blog.mapper.PostMapper;
import com.matan.blog.blog.model.Post;
import com.matan.blog.blog.model.User;
import com.matan.blog.blog.repository.PostRepository;
import com.matan.blog.blog.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final AuthService authService;

    @Transactional
    public void save(PostRequest postRequest) {
        User user = authService.getCurrentUser();
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


    @Transactional
    public void deletePostById(String id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException("post not fined whit that id: " + id));
        User user = authService.getCurrentUser();
        if (user.getPosts() != null) {
            List<Post> list = user.getPosts();
            list.remove(post);
            user.setPosts(list);
            postRepository.delete(post);
            userRepository.save(user);
        }
    }

    @Transactional
    public void editPost(EditPostRequest editPostRequest) {
        User user = authService.getCurrentUser();
        Post oldPost = postRepository.findById(editPostRequest.getPostId()).orElseThrow(() -> new PostNotFoundException("post not fined whit that id: " + editPostRequest.getPostId()));
        Post editPost = postMapper.mapEdit(editPostRequest, oldPost);

        List<Post> list = user.getPosts();
        assert list != null;
        list.remove(oldPost);
        list.add(editPost);
        user.setPosts(list);
        postRepository.save(editPost);
        userRepository.save(user);

    }

}
