package ru.yandex.practicum.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.model.Comment;
import ru.yandex.practicum.model.DTOObjects.CommentDTO;
import ru.yandex.practicum.model.DTOObjects.CreateCommentDTO;
import ru.yandex.practicum.repository.CommentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }


    public List<CommentDTO> getComments(Long postId) {
        return commentRepository.getComments(postId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public CommentDTO getComment(Long postId, Long commentId) {
        return convertToDTO(commentRepository.getComment(postId, commentId));
    }

    public CommentDTO createComment(Long postId, CreateCommentDTO createCommentDTO) {
        Comment comment = new Comment();
        comment.setPostId(postId);
        comment.setText(createCommentDTO.getText());

        return convertToDTO(commentRepository.saveComment(comment));
    }
    public CommentDTO updateComment(CommentDTO commentDTO) {
        Comment comment = new Comment(
                commentDTO.getId(),
                commentDTO.getText(),
                commentDTO.getPostId());
        return convertToDTO(commentRepository.saveComment(comment));
    }
    public void deleteComment(Long postId, Long commentId) {
        commentRepository.deletePost(postId, commentId);
    }
    private CommentDTO convertToDTO(Comment comment) {
        return new CommentDTO(
                comment.getId(),
                comment.getText(),
                comment.getPostId()
        );
    }

}