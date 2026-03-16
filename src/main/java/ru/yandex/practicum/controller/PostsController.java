package ru.yandex.practicum.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.model.DTOObjects.CreatePostDTO;
import ru.yandex.practicum.model.DTOObjects.PageDTO;
import ru.yandex.practicum.model.DTOObjects.PostDTO;
import ru.yandex.practicum.model.DTOObjects.UpdatePostDTO;
import ru.yandex.practicum.service.PostService;

import java.io.IOException;

@CrossOrigin(origins = "http://localhost")
@RestController
@RequestMapping("/api/posts")
public class PostsController {

    private final PostService service;

    public PostsController(PostService service) {
        this.service = service;
    }

    @GetMapping
    public PageDTO getPosts(@RequestParam(name = "search") String search
                        , @RequestParam(name = "pageNumber") Long pageNumber
                        , @RequestParam(name = "pageSize") Long pageSize) {
        return service.getPosts(search, pageNumber, pageSize);
    }
    @GetMapping(value = "/undefined/comments")
    public ResponseEntity<?> getUndefinedComment() {
        return ResponseEntity.badRequest().build();
    }
    @PostMapping(value = "/{id}")
    public PostDTO getPost(@PathVariable(name = "id") Long id) {
        return service.getPost(id);
    }
    @GetMapping(value = "/{id}")//фронт все время пытается тянуть посты методом get
    public PostDTO getPostGet(@PathVariable(name = "id") Long id) {
        return service.getPost(id);
    }

    @PostMapping
    public PostDTO createPost(@RequestBody CreatePostDTO createPostDTO) {
        return service.createPost(createPostDTO);
    }
    @PutMapping(value = "/{id}")
    public PostDTO updatePost(@PathVariable(name = "id") Long id, @RequestBody UpdatePostDTO updatePostDTO) {
        return service.updatePost(updatePostDTO);
    }
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable(name = "id") Long id) {
        service.deletePost(id);
        return ResponseEntity.ok().build();
    }
    @PostMapping(value = "/{id}/likes")
    public ResponseEntity<Integer> addLikes(@PathVariable(name = "id") Long id) {
        int likesCount = service.addLikes(id);
        return ResponseEntity.ok(likesCount);
    }

    @PutMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateImage(
            @PathVariable(name = "id") Long id,
            @RequestParam("image") MultipartFile file) throws IOException {
        service.updateImage(id, file);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{id}/image", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getImage(@PathVariable(name = "id") Long id) {
        byte[] image = service.getImage(id);
        return ResponseEntity.ok().body(image);
    }

}