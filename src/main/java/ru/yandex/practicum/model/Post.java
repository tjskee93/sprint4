package ru.yandex.practicum.model;

import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
public class Post {

    private Long id;
    private String title;
    private String text;
    private long likesCount;
    private String tags;
    private long commentsCount;
    private byte[] image;
    public Post() {}

    public Post(long id, String title, String text, long likesCount, String tags, long commentsCount, byte[] image) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.likesCount = likesCount;
        this.tags = tags;
        this.commentsCount = commentsCount;
        this.image = image;
    }


    public List<String> getTagsList() {
        if (tags == null || tags.isEmpty()) {
            return List.of();
        }
        return Arrays.asList(tags.split(" "));
    }

    public void setTagsList(List<String> tagsList) {
        if (tagsList == null || tagsList.isEmpty()) {
            this.tags = "";
        } else {
            this.tags = String.join(" ", tagsList);
        }
    }

    // Стандартные геттеры и сеттеры
    //Loombok @Data

} 