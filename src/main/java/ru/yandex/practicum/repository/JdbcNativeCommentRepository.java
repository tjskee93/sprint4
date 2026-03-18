package ru.yandex.practicum.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.Comment;
import ru.yandex.practicum.model.Post;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class JdbcNativeCommentRepository implements CommentRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcNativeCommentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    private static final RowMapper<Comment> commentRowMapper =
            (rs, rowNum) -> new Comment(
                    rs.getLong("id"),
                    rs.getString("text"),
                    rs.getLong("postId")
            );

    @Override
    public List<Comment> getComments(Long postId) {
        String query = "select cm.id, cm.text, cm.postId\n" +
                "from comments cm\n" +
                "where cm.postId = " + postId;
        List<Comment> comments = jdbcTemplate.query(query, commentRowMapper);
        return comments;
    }
    @Override
    public Comment getComment(Long postId, Long commentId) {
        String query = "select cm.id, cm.text, cm.postId\n" +
                "from comments cm\n" +
                "where cm.postId = " + postId + "\n" +
                "and cm.id = " + commentId;
        Comment comment = jdbcTemplate.queryForObject(query, commentRowMapper);
        return comment;
    }
    @Override
    public Comment saveComment(Comment comment){
        if (comment.getId() == null) {
            return insertComment(comment);
        } else {
            return updateComment(comment);
        }
    }

    private Comment insertComment(Comment comment) {
        String sqlComments = "insert into comments (text, postId) " +
                "values (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlComments, new String[]{"id"});
            ps.setString(1, comment.getText());
            ps.setLong(2, comment.getPostId());
            return ps;
        }, keyHolder);

        String sqlPosts = "update posts set commentsCount = commentsCount + 1 where id = ?";
        jdbcTemplate.update(sqlPosts, comment.getPostId());

        if (keyHolder.getKey() != null) {
            comment.setId(keyHolder.getKey().longValue());
        }
        
        return comment;
    }

    private Comment updateComment(Comment comment) {
        String sql = "update comments set text = ? where id = ? and postId = ?";
        System.out.println("обновляю Comment " + comment.toString());
        jdbcTemplate.update(sql,
                comment.getText(),
                comment.getId(),
                comment.getPostId());
        return comment;
    }
    public void deletePost(Long postId, Long commentId){
        String sqlComments = "delete from comments where id = ? and postId = ?";
        String sqlPosts = "update posts set commentsCount = commentsCount - 1 where id = ?";
        jdbcTemplate.update(sqlComments, commentId, postId);
        jdbcTemplate.update(sqlPosts, postId);
    }
}