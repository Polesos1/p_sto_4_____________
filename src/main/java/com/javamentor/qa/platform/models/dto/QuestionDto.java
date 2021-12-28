package com.javamentor.qa.platform.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Вопрос")
public class QuestionDto implements Serializable {

    private Long id;
    private String title;
    private Long authorId;
    private String authorName;
    private String authorImage;
    private String description;
    private int viewCount;
    private int countAnswer;
    private int countValuable;
    private LocalDateTime persistDateTime;
    private LocalDateTime lastUpdateDateTime;
    private List<TagDto> listTagDto;

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", title=" + title +
                ", authorId=" + authorId +
                ", authorName=" + authorName +
                ", authorImage=" + authorImage +
                ", description=" + description +
                ", viewCount=" + viewCount +
                ", countAnswer=" + countAnswer +
                ", countValuable=" + countValuable +
                ", persistDateTime=" + persistDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) +
                ", lastUpdateDateTime=" + lastUpdateDateTime +
                ", listTagDto=" + listTagDto +
                '}';
    }
}