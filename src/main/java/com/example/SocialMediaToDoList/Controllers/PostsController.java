package com.example.SocialMediaToDoList.Controllers;


import com.example.SocialMediaToDoList.Models.Posts;
import com.example.SocialMediaToDoList.Repository.PostsRepository;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("posts")
public class PostsController {
    @Autowired
    private  PostsRepository postsRepository;

    @GetMapping
    public String Testing () {
        return "This is working at the moment";
    }

    @PostMapping
    public ResponseEntity<Posts> makeNewPost (@RequestBody Posts post) {
        Posts newPost =  postsRepository.save(post);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/posts").buildAndExpand(newPost.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) throws Exception{
        if (postsRepository.existsById(id)) {
            postsRepository.deleteById(id);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/posts").buildAndExpand(postsRepository.findById(id).get().getId()).toUri();

            return new ResponseEntity<> ("Resource with ID " + id + " deleted successfully", HttpStatus.OK);
        } else {
            throw new Exception("Cannot delete post");
        }
    }



}
