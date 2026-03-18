package ru.yandex.practicum.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.model.DTOObjects.*;
import ru.yandex.practicum.service.CommentService;
import java.util.List;

@CrossOrigin(origins = "http://localhost")
@RestController
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {

    private final CommentService service;

    public CommentController(CommentService service) {
        this.service = service;
    }

    @GetMapping
    public List<CommentDTO> getComments(@PathVariable(name = "postId") Long postId) {
        return service.getComments(postId);
    }

    @GetMapping("/{commentId}")
    public CommentDTO getComment(
            @PathVariable(name = "postId") Long postId,
            @PathVariable(name = "commentId") Long commentId) {
        CommentDTO comment = service.getComment(postId, commentId);
        return comment;
    }
    @PostMapping
    public CommentDTO createComment(@PathVariable(name = "postId") Long postId,
                              @RequestBody CreateCommentDTO createCommentDTO) {
        return service.createComment(postId,createCommentDTO);
    }
    @PutMapping("/{commentId}")
    public CommentDTO updateComment(
            @PathVariable(name = "postId") Long postId,
            @PathVariable(name = "commentId") Long commentId,
            @RequestBody CommentDTO commentDTO) {
        CommentDTO comment = service.updateComment(commentDTO);
        return comment;
    }
    @DeleteMapping(value = "/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable(name = "postId") Long postId,
            @PathVariable(name = "commentId") Long commentId) {
        service.deleteComment(postId, commentId);
        return ResponseEntity.ok().build();
    }

}