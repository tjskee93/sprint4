package ru.yandex.practicum.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.DTOObjects.PageDTO;
import ru.yandex.practicum.model.DTOObjects.PostDTO;
import ru.yandex.practicum.model.Post;

import java.sql.PreparedStatement;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@Repository
public class JdbcNativePostRepository implements PostRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcNativePostRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    private static final RowMapper<Post> postRowMapper =
            (rs, rowNum) -> new Post(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("text"),
                    rs.getInt("likesCount"),
                    rs.getString("tags"),
                    rs.getInt("commentsCount"),
                    rs.getBytes("image")
            );

    @Override
    public List<Post> getPosts(String search, Long pageNumber, Long pageSize) {
        String query = "select ps.id, ps.title, ps.text, ps.likesCount, ps.tags, ps.commentsCount, ps.image\n" +
                "from posts ps\n" +
                "where ps.title like \'%" + search + "%\'";
        List<Post> posts = jdbcTemplate.query(query, postRowMapper);
        return posts;
    }
    @Override
    public Post getPost(Long id) {
        String query = "select ps.id, ps.title, ps.text, ps.likesCount, ps.tags, ps.commentsCount, ps.image\n" +
                "from posts ps\n" +
                "where ps.id = " + id;
        Post post = jdbcTemplate.queryForObject(query, postRowMapper);
        return post;
    }
    
    
    @Override
    public Post savePost(Post post) {
        if (post.getId() == null) {
            return insertPost(post);
        } else {
            return updatePost(post);
        }
    }

    public Post insertPost(Post post) {
        String sql = "insert into posts (title, text, likesCount, tags, commentsCount, image) " +
                "values (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, post.getTitle());
            ps.setString(2, post.getText());
            ps.setLong(3, post.getLikesCount());
            ps.setString(4, post.getTags());
            ps.setLong(5, post.getCommentsCount());
            ps.setBytes(6, post.getImage());
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            post.setId(keyHolder.getKey().longValue());
        }
        return post;
    }
    
    private Post updatePost(Post post) {
        String sql = "update posts set title = ?, text = ?, tags = ?, likesCount = ?, " +
                "commentsCount = ?, image = ? where id = ?";

        jdbcTemplate.update(sql,
                post.getTitle(),
                post.getText(),
                post.getTags(),
                post.getLikesCount(),
                post.getCommentsCount(),
                post.getImage(),
                post.getId());
        return post;
    }
    @Override
    public void deletePost(Long id) {
        String sql = "delete from posts where id = ?";
        jdbcTemplate.update(sql, id);
    }
    @Override
    public void addLikes(Long id){
        String sql = "update posts set likesCount = likesCount + 1 where id = ?";
        jdbcTemplate.update(sql, id);
    }
    @Override
    public int getLikes(Long id){
        String query = "select ps.likesCount\n" +
                "from posts ps\n" +
                "where ps.id = " + id;
        int likesCount = jdbcTemplate.queryForObject(query, Integer.class);
        return likesCount;
    }
    @Override
    public void updateImage(Long id, byte[] image) {
        String sql = "update posts set image = ? where id = ?";
        jdbcTemplate.update(sql, image, id);
    }
    @Override
    public byte[] getImage(Long id) {
        String sql = "select image from posts where id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> rs.getBytes("image"), id);
    }

}