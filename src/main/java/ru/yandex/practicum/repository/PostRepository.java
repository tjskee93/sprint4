package ru.yandex.practicum.repository;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.Post;

import java.util.List;

@Repository
public interface PostRepository {
    List<Post> getPosts(String search, Long pageNumber, Long pageSize);
    Post getPost(Long id);
    Post savePost(Post post);
    void deletePost(Long id);
    void addLikes(Long id);
    int getLikes(Long id);
    void updateImage(Long id, byte[] bytes);
    byte[] getImage(Long id);
}