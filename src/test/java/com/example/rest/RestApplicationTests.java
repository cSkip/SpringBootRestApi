package com.example.rest;

import com.example.rest.models.Post;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestApplicationTests {


    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private String getRootUrl() {
        return "http://localhost:" + port + "/api/v1";
    }

    @Test
    public void contextLoads() {
    }

    @Test
    public void testGetAllPosts() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/posts",
                HttpMethod.GET, entity, String.class);

        Assert.assertNotNull(response.getBody());
    }

    @Test
    public void testGetPostById() {
        Post post = restTemplate.getForObject(getRootUrl() + "/posts/1", Post.class);
        Assert.assertNotNull(post.getTitle());
    }

    @Test
    public void testCreatePost() {
        Post post = new Post();
        post.setId(3);
        post.setTitle("New Title");
        post.setBody("New Body");
        post.setDateCreated(new Date());
        post.setCreatedBy("Webuser");

        ResponseEntity<Post> postResponse = restTemplate.postForEntity(getRootUrl() + "/posts/create", post, Post.class);
        Assert.assertNotNull(postResponse);
        Assert.assertNotNull(postResponse.getBody());
        Assert.assertEquals("Title text mismatch", post.getTitle(), postResponse.getBody().getTitle());
        Assert.assertEquals("Body text mismatch", post.getBody(), postResponse.getBody().getBody());
    }

    @Test
    public void testUpdatePost() {
        int id = 1;
        Post post = restTemplate.getForObject(getRootUrl() + "/posts/" + id, Post.class);
        post.setTitle("New Title");
        post.setBody("New Body");

        restTemplate.put(getRootUrl() + "/posts/" + id, post);

        Post updatedPost = restTemplate.getForObject(getRootUrl() + "/posts/" + id, Post.class);

        Assert.assertNotNull(updatedPost);
        Assert.assertEquals("Title text mismatch", post.getTitle(), updatedPost.getTitle());
        Assert.assertEquals("Body text mismatch", post.getBody(), updatedPost.getBody());
    }

    @Test
    public void testDeletePost() {
        int id = 2;
        Post post = restTemplate.getForObject(getRootUrl() + "/posts/" + id, Post.class);
        Assert.assertNotNull(post);

        restTemplate.delete(getRootUrl() + "/posts/" + id);

        try {
            post = restTemplate.getForObject(getRootUrl() + "/posts/" + id, Post.class);
        } catch (final HttpClientErrorException e) {
            Assert.assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
        }
    }
}
