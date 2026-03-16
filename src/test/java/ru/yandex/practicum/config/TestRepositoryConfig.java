package ru.yandex.practicum.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.repository.CommentRepository;
import ru.yandex.practicum.repository.JdbcNativeCommentRepository;
import ru.yandex.practicum.repository.JdbcNativePostRepository;
import ru.yandex.practicum.repository.PostRepository;

@TestConfiguration
public class TestRepositoryConfig {

    @Bean
    public PostRepository postRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcNativePostRepository(jdbcTemplate);
    }

    @Bean
    public CommentRepository commentRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcNativeCommentRepository(jdbcTemplate);
    }
}
