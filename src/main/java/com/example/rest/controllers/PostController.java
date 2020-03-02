package com.example.rest.controllers;

import com.example.rest.models.Post;
import com.example.rest.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1")
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @GetMapping("/posts")
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable(value = "id") Long postId) throws Exception {
        Post post = postRepository.findById(postId).orElseThrow(() -> new Exception("Post not found, id = " + postId));

        return ResponseEntity.ok().body(post);
    }

    @PostMapping("/posts/create")
    public Post createPost(@Valid @RequestBody Post post) {
        return postRepository.save(post);
    }

    @PutMapping("/posts/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable(value = "id") Long postId, @Valid @RequestBody Post postDetails) throws Exception {
        Post post = postRepository.findById(postId).orElseThrow(() -> new Exception("Post not found, id = " + postId));

        post.setBody(postDetails.getBody());
        post.setTitle(postDetails.getTitle());
        post.setCreatedBy("Webuser");
        post.setDateCreated(new Date());
        Post updatedPost = postRepository.save(post);
        return ResponseEntity.ok(updatedPost);

    }

    @DeleteMapping("/post/{id}")
    public Map<String, Boolean> deleteUser(@PathVariable(value = "id") Long postId) throws Exception {
        Post post = postRepository.findById(postId).orElseThrow(() -> new Exception("Post not found, id = " + postId));
        postRepository.delete(post);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
