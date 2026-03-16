package ru.yandex.practicum.model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PostModelTest {
    @Test
    void setTagsList_ShouldConvertToListAndBack() {
        Post post = new Post();
        List<String> tags = Arrays.asList("Тег1", "Тег2", "Тег3");

        post.setTagsList(tags);
        List<String> retrievedTags = post.getTagsList();

        assertEquals(tags, retrievedTags);
        assertEquals("Тег1 Тег2 Тег3", post.getTags());
    }

    @Test
    void setTagsList_WithEmptyList_ShouldSetEmptyString() {
        Post post = new Post();
        List<String> emptyTags = List.of();

        post.setTagsList(emptyTags);

        assertTrue(post.getTagsList().isEmpty());
        assertEquals("", post.getTags());
    }

    @Test
    void setTagsList_WithNull_ShouldSetEmptyString() {
        Post post = new Post();

        post.setTagsList(null);

        assertTrue(post.getTagsList().isEmpty());
        assertEquals("", post.getTags());
    }

    @Test
    void getTagsList_WithNullTags_ShouldReturnEmptyList() {
        Post post = new Post();
        post.setTags(null);

        List<String> tags = post.getTagsList();

        assertNotNull(tags);
        assertTrue(tags.isEmpty());
    }

    @Test
    void getTagsList_WithEmptyTags_ShouldReturnEmptyList() {
        Post post = new Post();
        post.setTags("");

        List<String> tags = post.getTagsList();

        assertNotNull(tags);
        assertTrue(tags.isEmpty());
    }

    @Test
    void postConstructor_ShouldSetAllFields() {
        byte[] image = new byte[]{1, 2, 3};

        Post post = new Post(1L, "Пост", "Текст", 5L, "Тэг1 Тэг2", 3L, image);

        assertEquals(1L, post.getId());
        assertEquals("Пост", post.getTitle());
        assertEquals("Текст", post.getText());
        assertEquals(5L, post.getLikesCount());
        assertEquals("Тэг1 Тэг2", post.getTags());
        assertEquals(3L, post.getCommentsCount());
        assertArrayEquals(image, post.getImage());
    }
}
