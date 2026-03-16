package ru.yandex.practicum.model.DTOObjects;

import lombok.Data;
import java.util.List;
@Data
public class PostDTO {
    private Long id;
    private String title;
    private String text;
    private long likesCount;
    private List<String> tags;
    private long commentsCount;

    public PostDTO(Long id, String title, String text, List<String> tags,
                   long likesCount, long commentsCount) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.tags = tags;
        this.likesCount = likesCount;
        this.commentsCount = commentsCount;
    }
    public PostDTO() {}

}
