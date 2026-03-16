package ru.yandex.practicum.repository;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.Comment;
import ru.yandex.practicum.model.Post;

import java.util.List;

@Repository
public interface CommentRepository {
    List<Comment> getComments(Long postId);
    Comment getComment(Long postId, Long commentId);
    Comment saveComment(Comment comment);
    void deletePost(Long postId, Long commentId);
}