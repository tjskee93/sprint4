package ru.yandex.practicum.model.DTOObjects;

import lombok.Data;
import java.util.List;
@Data
public class CreatePostDTO {
    private String title;
    private String text;
    private List<String> tags;
    
    public CreatePostDTO(String title, String text, List<String> tags){
        this.title = title;
        this.text = text;
        this.tags = tags;
    }
    public CreatePostDTO() {}
}
