package com.example.spring_security.services;

import java.util.List;

import com.example.spring_security.dto.PostDto;

public interface PostService {

    List<PostDto> getAllPosts();

    PostDto createNewPost(PostDto inputPost);

    PostDto getPostById(Long postId);

}
