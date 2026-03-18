package ru.yandex.practicum.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jdbc.test.autoconfigure.DataJdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.config.TestRepositoryConfig;
import ru.yandex.practicum.model.Post;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJdbcTest
@Import(TestRepositoryConfig.class)
@Sql(scripts = "/clean.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class JdbcNativePostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Test
    void savePost_ShouldInsertNewPost() {
        Post post = createTestPost("Тестовый пост", "Тут текст", List.of("тэг1", "тэг2"));

        Post savedPost = postRepository.savePost(post);

        assertNotNull(savedPost.getId());
        assertEquals("Тестовый пост", savedPost.getTitle());
        assertEquals("тэг1 тэг2", savedPost.getTags());

        Post dbPost = postRepository.getPost(savedPost.getId());
        assertNotNull(dbPost);
    }

    @Test
    void getPost_ShouldReturnCorrectPost() {
        Post savedPost = postRepository.savePost(createTestPost("Тестовый пост", "Тут текст", List.of("тэг1")));

        Post retrievedPost = postRepository.getPost(savedPost.getId());

        assertNotNull(retrievedPost);
        assertEquals(savedPost.getId(), retrievedPost.getId());
        assertEquals("Тестовый пост", retrievedPost.getTitle());
    }

    @Test
    void getPosts_WithSearch_ShouldReturnFilteredResults() {
        postRepository.savePost(createTestPost("Тестовый пост1", "Тут текст1", List.of("тэг1")));
        postRepository.savePost(createTestPost("Тестовый пост2", "Тут текст2", List.of("тэг2")));
        postRepository.savePost(createTestPost("Тестовый пост3", "Тут текст3", List.of("тэг3")));
        
        List<Post> posts = postRepository.getPosts("2", 1L, 5L);
        
        assertEquals(1, posts.size());
        assertTrue(posts.stream().allMatch(p -> p.getTitle().contains("Тестовый пост2")));
    }

    @Test
    void updatePost_ShouldModifyExistingPost() {
        Post savedPost = postRepository.savePost(createTestPost("Тестовый пост", "Тут текст", List.of("Тег1")));

        savedPost.setTitle("Тестовый пост обновлен");
        savedPost.setText("Тут текст обновлен");
        savedPost.setTagsList(List.of("Тег1обновлен"));
        Post updatedPost = postRepository.savePost(savedPost);

        assertEquals("Тестовый пост обновлен", updatedPost.getTitle());
        assertEquals("Тут текст обновлен", updatedPost.getText());
        assertEquals("Тег1обновлен", updatedPost.getTags());
    }

    @Test
    void addLikes_ShouldIncrementLikesCount() {
        Post savedPost = postRepository.savePost(createTestPost("Тестовый пост", "Тут текст", List.of("Тег1")));

        postRepository.addLikes(savedPost.getId());
        int likesCount = postRepository.getLikes(savedPost.getId());

        assertEquals(1, likesCount);
    }

    @Test
    void deletePost_ShouldRemovePost() {
        Post savedPost = postRepository.savePost(createTestPost("Тестовый пост", "Тут текст", List.of("Тег1")));

        postRepository.deletePost(savedPost.getId());

        assertThrows(Exception.class, () -> postRepository.getPost(savedPost.getId()));
    }

    private Post createTestPost(String title, String text, List<String> tags) {
        Post post = new Post();
        post.setTitle(title);
        post.setText(text);
        post.setTagsList(tags);
        post.setLikesCount(0);
        post.setCommentsCount(0);
        return post;
    }
}
