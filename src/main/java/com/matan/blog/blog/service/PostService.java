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
        //get the post from the end of the array need to check
        int listSize = postRepository.findAll().size();
        int availablePage = (int) Math.floor((double) listSize / 10);
        if ((availablePage - page) <= -1){
            page=0;
            availablePage += 1;
        }
        return postRepository.findAll(PageRequest.of(availablePage - page, 10/*, Sort.by(Sort.Direction.ASC, "created")*/)).stream().map(postMapper::mapToResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PostResponse getPost(String id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException("post not fined whit that id: " + id));
        return postMapper.mapToResponse(post);
    }


    @Transactional
    public void deletePostById(String id) {
        Post oldPost = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException("post not fined whit that id: " + id));
        User user = authService.getCurrentUser();
        if (user.getPosts() != null) {
            List<Post> list = user.getPosts();
            list.removeIf(post -> post.get_id().equals(oldPost.get_id()));

            user.setPosts(list);
            postRepository.delete(oldPost);
            userRepository.save(user);
        }
    }

    @Transactional
    public void editPost(EditPostRequest editPostRequest) {
        User user = authService.getCurrentUser();
        Post oldPost = postRepository.findById(editPostRequest.getPostId()).orElseThrow(() -> new PostNotFoundException("post not fined whit that id: " + editPostRequest.getPostId()));
        Post editPost = postMapper.mapEdit(editPostRequest, oldPost);

        List<Post> list = user.getPosts();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).get_id().equals(oldPost.get_id())) {
                    list.set(i, editPost);
                    break;
                }
            }
        }

        user.setPosts(list);
        postRepository.save(editPost);
        userRepository.save(user);

    }

}
