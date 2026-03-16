package ru.yandex.practicum.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.config.IntegrationTest;
import ru.yandex.practicum.model.DTOObjects.CreatePostDTO;
import ru.yandex.practicum.model.DTOObjects.PageDTO;
import ru.yandex.practicum.model.DTOObjects.PostDTO;
import ru.yandex.practicum.model.DTOObjects.UpdatePostDTO;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@IntegrationTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Test
    void createPost_ShouldReturnCreatedPost() {
        CreatePostDTO createDTO = new CreatePostDTO(
                "Тестовый пост",
                "Текст",
                List.of("Тег1", "Тег2")
        );

        PostDTO result = postService.createPost(createDTO);

        assertNotNull(result.getId());
        assertEquals("Тестовый пост", result.getTitle());
        assertEquals("Текст", result.getText());
        assertEquals(2, result.getTags().size());
        assertEquals(0, result.getLikesCount());
        assertEquals(0, result.getCommentsCount());
    }
    
    @Test
    void getPost_ShouldReturnCorrectPost() {
        PostDTO created = createTestPost("Тестовый пост", "Текст", List.of("Тег1"));

        PostDTO retrieved = postService.getPost(created.getId());

        assertNotNull(retrieved);
        assertEquals(created.getId(), retrieved.getId());
        assertEquals("Тестовый пост", retrieved.getTitle());
    }

    @Test
    void updatePost_ShouldModifyExistingPost() {
        PostDTO created = createTestPost("Тестовый пост", "Текст", List.of("Тег1"));

        UpdatePostDTO updateDTO = new UpdatePostDTO(
                created.getId(),
                "Тестовый пост обновление",
                "Текст обновление",
                List.of("Тег1обновление")
        );

        PostDTO updated = postService.updatePost(updateDTO);

        assertEquals(created.getId(), updated.getId());
        assertEquals("Тестовый пост обновление", updated.getTitle());
        assertEquals("Текст обновление", updated.getText());
        assertEquals(List.of("Тег1обновление"), updated.getTags());
    }

    @Test
    void deletePost_ShouldRemovePost() {
        PostDTO created = createTestPost("Тестовый пост", "Текст", List.of("Тег1"));

        postService.deletePost(created.getId());

        assertThrows(Exception.class, () -> postService.getPost(created.getId()));
    }

    @Test
    void addLikes_ShouldIncrementAndReturnNewCount() {
        PostDTO created = createTestPost("Тестовый пост", "Текст", List.of("Тег1"));
        assertEquals(0, created.getLikesCount());

        int likesCount = postService.addLikes(created.getId());

        assertEquals(1, likesCount);

        PostDTO updated = postService.getPost(created.getId());
        assertEquals(1, updated.getLikesCount());
    }

    @Test
    void getPosts_WithPagination_ShouldReturnCorrectPages() {
        createMultipleTestPosts();

        PageDTO page1 = postService.getPosts("", 1L, 2L);
        PageDTO page2 = postService.getPosts("", 2L, 2L);

        assertNotNull(page1);
        assertNotNull(page2);
        assertEquals(2, page1.getPosts().size());

        assertFalse(page1.isHasPrev());

        if (!page2.getPosts().isEmpty()) {
            assertTrue(page2.isHasPrev());
        }
    }

    @Test
    void getPosts_WithSearch_ShouldFilterResults() {
        createMultipleTestPosts();

        PageDTO javaPosts = postService.getPosts("Текст1", 1L, 10L);
        PageDTO springPosts = postService.getPosts("Пост2", 1L, 10L);

        assertTrue(javaPosts.getPosts().stream()
                .allMatch(p -> p.getTitle().contains("Пост1") && p.getText().contains("Текст1")));

        assertTrue(springPosts.getPosts().stream()
                .allMatch(p -> p.getTitle().contains("Пост2") && p.getText().contains("Текст2")));
    }

    private PostDTO createTestPost(String title, String text, List<String> tags) {
        CreatePostDTO createDTO = new CreatePostDTO(title, text, tags);
        return postService.createPost(createDTO);
    }

    private void createMultipleTestPosts() {
        postService.createPost(new CreatePostDTO("Пост1", "Текст1", List.of("Тег1")));
        postService.createPost(new CreatePostDTO("Пост2", "Текст2", List.of("Тег2")));
        postService.createPost(new CreatePostDTO("Пост3", "Текст3", List.of("Тег3")));
        postService.createPost(new CreatePostDTO("Пост4", "Текст4", List.of("Тег4", "Тег5")));
        postService.createPost(new CreatePostDTO("Пост5", "Текст5", List.of("Тег6")));
    }
}
