package ru.yandex.practicum.model;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.model.DTOObjects.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DTOModelTest {
    @Test
    void postDTO_ShouldStoreAndRetrieveData() {
        List<String> tags = Arrays.asList("Тег1", "Тег2");

        PostDTO postDTO = new PostDTO(1L, "Пост1", "Текст", tags, 5L, 3L);

        assertEquals(1L, postDTO.getId());
        assertEquals("Пост1", postDTO.getTitle());
        assertEquals("Текст", postDTO.getText());
        assertEquals(tags, postDTO.getTags());
        assertEquals(5L, postDTO.getLikesCount());
        assertEquals(3L, postDTO.getCommentsCount());
    }

    @Test
    void createPostDTO_ShouldStoreAndRetrieveData() {
        List<String> tags = Arrays.asList("Тег1", "Тег2");

        CreatePostDTO createDTO = new CreatePostDTO("Пост1", "Текст", tags);

        assertEquals("Пост1", createDTO.getTitle());
        assertEquals("Текст", createDTO.getText());
        assertEquals(tags, createDTO.getTags());
    }

    @Test
    void updatePostDTO_ShouldStoreAndRetrieveData() {
        List<String> tags = Arrays.asList("Тег1");

        UpdatePostDTO updateDTO = new UpdatePostDTO(1L, "Пост1", "Текст", tags);

        assertEquals(1L, updateDTO.getId());
        assertEquals("Пост1", updateDTO.getTitle());
        assertEquals("Текст", updateDTO.getText());
        assertEquals(tags, updateDTO.getTags());
    }

    @Test
    void pageDTO_ShouldStoreAndRetrieveData() {
        List<PostDTO> posts = Arrays.asList(
                new PostDTO(1L, "Пост1", "Текст1", List.of("Тег1"), 0L, 0L),
                new PostDTO(2L, "Пост2", "Текст2", List.of("Тег2"), 0L, 0L)
        );

        PageDTO pageDTO = new PageDTO(posts, true, false, 5L);

        assertEquals(2, pageDTO.getPosts().size());
        assertTrue(pageDTO.isHasPrev());
        assertFalse(pageDTO.isHasNext());
        assertEquals(5L, pageDTO.getLastPage());
    }

    @Test
    void commentDTO_ShouldStoreAndRetrieveData() {
        CommentDTO commentDTO = new CommentDTO(1L, "Коммент", 2L);

        assertEquals(1L, commentDTO.getId());
        assertEquals("Коммент", commentDTO.getText());
        assertEquals(2L, commentDTO.getPostId());
    }

    @Test
    void createCommentDTO_ShouldStoreAndRetrieveData() {
        CreateCommentDTO createDTO = new CreateCommentDTO("Коммент", 1L);

        assertEquals("Коммент", createDTO.getText());
        assertEquals(1L, createDTO.getPostId());
    }

}
