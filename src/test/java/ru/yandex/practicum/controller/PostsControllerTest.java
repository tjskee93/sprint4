package ru.yandex.practicum.controller;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.yandex.practicum.config.IntegrationTest;
import ru.yandex.practicum.model.DTOObjects.CreatePostDTO;
import ru.yandex.practicum.model.DTOObjects.UpdatePostDTO;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@AutoConfigureMockMvc
class PostsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createPost_ShouldReturnCreatedPost() throws Exception {
        CreatePostDTO createDTO = new CreatePostDTO(
                "Тестовый пост",
                "Текст",
                List.of("Тег1", "Тег2")
        );

        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("Тестовый пост"))
                .andExpect(jsonPath("$.text").value("Текст"))
                .andExpect(jsonPath("$.tags.length()").value(2))
                .andExpect(jsonPath("$.likesCount").value(0))
                .andExpect(jsonPath("$.commentsCount").value(0));
    }

    @Test
    void getPost_ShouldReturnPost() throws Exception {
        Long postId = createTestPost("Тестовый пост", "Текст", List.of("Тег1"));

        mockMvc.perform(get("/api/posts/{id}", postId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(postId))
                .andExpect(jsonPath("$.title").value("Тестовый пост"))
                .andExpect(jsonPath("$.text").value("Текст"));
    }

    @Test
    void getPosts_WithPagination_ShouldReturnPageDTO() throws Exception {
        createTestPost("Пост1", "Текст1", List.of("Тег1"));
        createTestPost("Пост2", "Текст2", List.of("Тег2"));
        createTestPost("Пост3", "Текст3", List.of("Тег3"));

        mockMvc.perform(get("/api/posts")
                        .param("search", "")
                        .param("pageNumber", "1")
                        .param("pageSize", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.posts").isArray())
                .andExpect(jsonPath("$.posts.length()").value(2))
                .andExpect(jsonPath("$.hasPrev").value(false))
                .andExpect(jsonPath("$.hasNext").exists())
                .andExpect(jsonPath("$.lastPage").exists());
    }

    @Test
    void updatePost_ShouldReturnUpdatedPost() throws Exception {
        Long postId = createTestPost("Тестовый пост", "Текст", List.of("Тег1"));

        UpdatePostDTO updateDTO = new UpdatePostDTO(
                postId,
                "Тестовый пост обновление",
                "Текст обновление",
                List.of("Тег1обновление")
        );

        mockMvc.perform(put("/api/posts/{id}", postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(postId))
                .andExpect(jsonPath("$.title").value("Тестовый пост обновление"))
                .andExpect(jsonPath("$.text").value("Текст обновление"))
                .andExpect(jsonPath("$.tags[0]").value("Тег1обновление"));
    }

    @Test
    void deletePost_ShouldReturnOk() throws Exception {
        Long postId = createTestPost("Тестовый пост", "Текст", List.of("Тег1"));

        mockMvc.perform(delete("/api/posts/{id}", postId))
                .andExpect(status().isOk());
    }

    @Test
    void addLikes_ShouldReturnUpdatedCount() throws Exception {
        Long postId = createTestPost("Тестовый пост", "Текст", List.of("Тег1"));

        mockMvc.perform(post("/api/posts/{id}/likes", postId))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    void updateAndGetImage_ShouldWorkCorrectly() throws Exception {
        Long postId = createTestPost("Тестовый пост", "Текст", List.of("Тег1"));

        MockMultipartFile file = new MockMultipartFile(
                "image",
                "test.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "Картинка".getBytes()
        );

        mockMvc.perform(multipart("/api/posts/{id}/image", postId)
                        .file(file)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        }))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/posts/{id}/image", postId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_JPEG))
                .andExpect(content().bytes("Картинка".getBytes()));
    }

    @Test
    void searchPosts_WithFilter_ShouldReturnFilteredResults() throws Exception {
        createTestPost("Пост1", "Текст1", List.of("Тег1"));
        createTestPost("Пост2", "Текст2", List.of("Тег2"));
        createTestPost("Пост3", "Текст3", List.of("Тег3"));

        mockMvc.perform(get("/api/posts")
                        .param("search", "2")
                        .param("pageNumber", "1")
                        .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.posts.length()").value(1))
                .andExpect(jsonPath("$.posts[0].title").value("Пост2"))
                .andExpect(jsonPath("$.posts[0].text").value("Текст2"));
    }

    private Long createTestPost(String title, String text, List<String> tags) throws Exception {
        CreatePostDTO createDTO = new CreatePostDTO(title, text, tags);

        MvcResult result = mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(jsonResponse);

        JsonNode idNode = jsonNode.get("id");
        assertNotNull(idNode, "Вернулся null");

        return idNode.asLong();
    }
}
