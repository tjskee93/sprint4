package ru.yandex.practicum.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jdbc.test.autoconfigure.DataJdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.config.TestRepositoryConfig;
import ru.yandex.practicum.model.Comment;
import ru.yandex.practicum.model.Post;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJdbcTest
@Import(TestRepositoryConfig.class)
@Sql(scripts = "/clean.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class JdbcNativeCommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    private Long testPostId;

    @BeforeEach
    void newTestPost() {
        Post post = new Post();
        post.setTitle("Тестовый пост");
        post.setText("Тут текст");
        post.setTagsList(List.of("Тег1"));
        testPostId = postRepository.savePost(post).getId();
    }

    @Test
    void saveComment_ShouldInsertNewComment() {
        Comment comment = createTestComment("Тестовый коммент", testPostId);

        Comment savedComment = commentRepository.saveComment(comment);

        assertNotNull(savedComment.getId());
        assertEquals("Тестовый коммент", savedComment.getText());
        assertEquals(testPostId, savedComment.getPostId());

        Post updatedPost = postRepository.getPost(testPostId);
        assertEquals(1, updatedPost.getCommentsCount());
    }

    @Test
    void getComments_ShouldReturnAllCommentsForPost() {
        commentRepository.saveComment(createTestComment("Коммент1", testPostId));
        commentRepository.saveComment(createTestComment("Коммент2", testPostId));
        commentRepository.saveComment(createTestComment("Коммент3", testPostId));

        List<Comment> comments = commentRepository.getComments(testPostId);

        assertEquals(3, comments.size());
    }

    @Test
    void getComment_ShouldReturnSpecificComment() {
        Comment savedComment = commentRepository.saveComment(
                createTestComment("Коммент", testPostId)
        );

        Comment retrievedComment = commentRepository.getComment(testPostId, savedComment.getId());

        assertNotNull(retrievedComment);
        assertEquals(savedComment.getId(), retrievedComment.getId());
        assertEquals("Коммент", retrievedComment.getText());
    }

    @Test
    void updateComment_ShouldModifyExistingComment() {
        Comment savedComment = commentRepository.saveComment(
                createTestComment("Коммент", testPostId)
        );

        savedComment.setText("Обновленный коммент");
        Comment updatedComment = commentRepository.saveComment(savedComment);

        assertEquals("Обновленный коммент", updatedComment.getText());
    }

    @Test
    void deleteComment_ShouldRemoveCommentAndDecrementCount() {
        Comment savedComment = commentRepository.saveComment(
                createTestComment("Коммент", testPostId)
        );
        assertEquals(1, postRepository.getPost(testPostId).getCommentsCount());

        commentRepository.deletePost(testPostId, savedComment.getId());

        List<Comment> comments = commentRepository.getComments(testPostId);
        assertTrue(comments.isEmpty());
        assertEquals(0, postRepository.getPost(testPostId).getCommentsCount());
    }

    private Comment createTestComment(String text, Long postId) {
        Comment comment = new Comment();
        comment.setText(text);
        comment.setPostId(postId);
        return comment;
    }
}
