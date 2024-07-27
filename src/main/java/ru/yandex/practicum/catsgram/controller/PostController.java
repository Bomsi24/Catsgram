package ru.yandex.practicum.catsgram.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.service.PostService;

import java.util.Collection;
import java.util.Optional;

@RestController
public class PostController {
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/posts")
    public Collection<Post> findAll(@RequestParam(defaultValue = "normal") String sort,
                                    @RequestParam(defaultValue = "0") int size,
                                    @RequestParam(defaultValue = "0") int from) {
        return postService.findAll(sort, size, from);
    }


    @GetMapping(value = "/post/{postId}")
    public Optional<Post> getPostId(@PathVariable long postId) {
        Optional<Post> postOptional = postService.findById(postId);
        if (postOptional.isPresent()) {
            return postOptional;
        } else {
            throw new NotFoundException(String.format("Пост № %d не найден", postId));
        }
    }


    @PostMapping("/posts")
    @ResponseStatus(HttpStatus.CREATED)
    public Post create(@RequestBody Post post) {
        return postService.create(post);
    }

    @PutMapping("/posts")
    public Post update(@RequestBody Post newPost) {
        return postService.update(newPost);
    }
}