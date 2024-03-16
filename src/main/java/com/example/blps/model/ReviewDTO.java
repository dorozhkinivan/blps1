package com.example.blps.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReviewDTO {
    private Long recipeId;
    private Integer rating;
    private String comment;

    /**
     * Проверяет, содержит ли DTO незаполненные поля.
     * Метод полезен для валидации входных данных перед обработкой.
     *
     * @return true, если хотя бы одно из полей равно null, иначе false.
     */
    public boolean hasNulls() {
        return this.recipeId == null || this.rating == null || this.comment == null;
    }
}
