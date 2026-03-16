package ru.yandex.practicum.model;

import lombok.Data;

@Data
public class Comment {
    private Long id;
    private String text;
    private Long postId;
    public Comment() {}
    public Comment(Long id, String text, Long postId) {
        this.id = id;
        this.text = text;
        this.postId = postId;
    }

}
