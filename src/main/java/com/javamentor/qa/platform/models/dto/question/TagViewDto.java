package com.javamentor.qa.platform.models.dto.question;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(description = "TagViewDto - дання ДТО исползуется для пагинации тегов")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagViewDto {

    @Schema(description = "Идентификатор")
    private Long id;

    @Schema(description = "Имя тега")
    private String name;

    private String description;

    @Schema(description = "Дата сохранения тега")
    private LocalDateTime persistDateTime;

    @Schema(description = "Количество вопросов по тегу")
    private Long countQuestion;

    public TagViewDto(Long id, String name, LocalDateTime persistDateTime) {
        this.id = id;
        this.name = name;
        this.persistDateTime = persistDateTime;
    }

    public TagViewDto(Long id, String name, LocalDateTime persistDateTime, Long countQuestion) {
        this.id = id;
        this.name = name;
        this.persistDateTime = persistDateTime;
        this.countQuestion = countQuestion;
    }
}
