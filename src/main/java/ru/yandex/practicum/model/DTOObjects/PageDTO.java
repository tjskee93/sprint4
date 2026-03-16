package ru.yandex.practicum.model.DTOObjects;

import lombok.Data;
import java.util.List;

@Data
public class PageDTO {
    private List<PostDTO> posts;
    boolean hasPrev;
    boolean hasNext;
    private long lastPage;

    public PageDTO(List<PostDTO> posts, boolean hasPrev, boolean hasNext, long lastPage) {
        this.posts = posts;
        this.hasPrev = hasPrev;
        this.hasNext = hasNext;
        this.lastPage = lastPage;
    }
    public PageDTO() {}
}
