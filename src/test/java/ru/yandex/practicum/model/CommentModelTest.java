package ru.yandex.practicum.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommentModelTest {
    @Test
    void comment_ShouldStoreAndRetrieveData() {
        Comment comment = new Comment(1L, "Коммент", 2L);

        assertEquals(1L, comment.getId());
        assertEquals("Коммент", comment.getText());
        assertEquals(2L, comment.getPostId());
    }
}
