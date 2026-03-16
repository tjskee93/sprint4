package ru.yandex.practicum.model.DTOObjects;

import lombok.Data;
import java.util.List;
@Data
public class UpdatePostDTO {
    private Long id;
    private String title;
    private String text;
    private List<String> tags;
    public UpdatePostDTO(Long id, String title, String text, List<String> tags) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.tags = tags;
    }
    public UpdatePostDTO(){}
}
