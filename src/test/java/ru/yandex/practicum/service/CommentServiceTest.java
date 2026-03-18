package ru.yandex.practicum.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.config.IntegrationTest;
import ru.yandex.practicum.model.DTOObjects.CommentDTO;
import ru.yandex.practicum.model.DTOObjects.CreateCommentDTO;
import ru.yandex.practicum.model.DTOObjects.CreatePostDTO;
import ru.yandex.practicum.model.DTOObjects.PostDTO;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@IntegrationTest
class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private PostService postService;

    private Long testPostId;

    @BeforeEach
    void newTestPost() {
        CreatePostDTO createPost = new CreatePostDTO(
                "Тестовый пост",
                "Текст",
                List.of("Тег1")
        );
        testPostId = postService.createPost(createPost).getId();
    }

    @Test
    void createComment_ShouldReturnCreatedComment() {
        CreateCommentDTO createDTO = new CreateCommentDTO("Коммент", testPostId);

        CommentDTO result = commentService.createComment(testPostId, createDTO);

        assertNotNull(result.getId());
        assertEquals("Коммент", result.getText());
        assertEquals(testPostId, result.getPostId());

        PostDTO updatedPost = postService.getPost(testPostId);
        assertEquals(1, updatedPost.getCommentsCount());
    }

    @Test
    void getComments_ShouldReturnAllComments() {
        createTestComment("Коммент1", testPostId);
        createTestComment("Коммент2", testPostId);
        createTestComment("Коммент3", testPostId);

        List<CommentDTO> comments = commentService.getComments(testPostId);

        assertEquals(3, comments.size());
        assertTrue(comments.stream().allMatch(c -> c.getPostId().equals(testPostId)));
    }

    @Test
    void getComment_ShouldReturnSpecificComment() {
        CommentDTO created = createTestComment("Коммент", testPostId);

        CommentDTO retrieved = commentService.getComment(testPostId, created.getId());

        assertNotNull(retrieved);
        assertEquals(created.getId(), retrieved.getId());
        assertEquals("Коммент", retrieved.getText());
    }

    @Test
    void updateComment_ShouldModifyExistingComment() {
        CommentDTO created = createTestComment("Коммент", testPostId);

        CommentDTO updateDTO = new CommentDTO(
                created.getId(),
                "Коммент обновленный",
                testPostId
        );

        CommentDTO updated = commentService.updateComment(updateDTO);

        assertEquals(created.getId(), updated.getId());
        assertEquals("Коммент обновленный", updated.getText());
    }

    @Test
    void deleteComment_ShouldRemoveComment() {
        CommentDTO created = createTestComment("Коммент", testPostId);
        assertEquals(1, postService.getPost(testPostId).getCommentsCount());

        commentService.deleteComment(testPostId, created.getId());

        List<CommentDTO> remainingComments = commentService.getComments(testPostId);
        assertTrue(remainingComments.isEmpty());
        assertEquals(0, postService.getPost(testPostId).getCommentsCount());
    }

    private CommentDTO createTestComment(String text, Long postId) {
        CreateCommentDTO createDTO = new CreateCommentDTO(text, postId);
        return commentService.createComment(postId, createDTO);
    }
}
