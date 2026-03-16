package ru.yandex.practicum.model.DTOObjects;

import lombok.Data;

@Data
public class CreateCommentDTO {
    private String text;
    private Long postId;

    public CreateCommentDTO(String text, Long postId) {
        this.text = text;
        this.postId = postId;
    }
    public CreateCommentDTO() {}
}
