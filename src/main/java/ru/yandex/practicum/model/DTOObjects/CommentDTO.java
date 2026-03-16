package ru.yandex.practicum.model.DTOObjects;

import lombok.Data;

@Data
public class CommentDTO {
    private Long id;
    private String text;
    private Long postId;

    public CommentDTO(Long id, String text, Long postId) {
        this.id = id;
        this.text = text;
        this.postId = postId;
    }
    public CommentDTO() {}
}
