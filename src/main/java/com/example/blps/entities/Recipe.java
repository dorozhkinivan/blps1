package com.example.blps.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * Класс сущности, представляющий рецепт в системе.
 */
@Entity
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "recipes")
public class Recipe {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "owner_id", nullable = false)
    private Long ownerId;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "recipe_id")
    private List<RecipeStep> steps;
    @Column(name = "main_photo_url")
    private String mainPhotoUrl;
    @ElementCollection
    @Column(name = "tags")
    private List<String> tags;
    @Column(name = "category")
    private String category;
    @Column(name = "time_to_cook")
    private Integer timeToCook;
    @Column(name = "numbe_of_servings")
    private Integer numberOfServings;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "recipe_id")
    private List<Ingredient> ingredients;
    @Column(name = "moder_comment")
    private String moderComment;
    @Column(name = "moder_status", nullable = false)
    private Boolean moderStatus;  // true - значит надо промодерировать
    @Column(name = "is_draft", nullable = false)
    private Boolean isDraft;
    @Column(name = "views", nullable = false)
    private Integer views;
    /**
     * Проверяет, содержит ли рецепт все необходимые поля для публикации.
     *
     * @return true, если рецепт не готов к публикации (имеет незаполненные поля).
     */
    public boolean hasNullFieldsForDraft() {
        if (this.mainPhotoUrl == null || this.category == null || this.timeToCook == null || this.views == null ||
                this.numberOfServings == null) {
            return true;
        }
        return this.steps == null || this.steps.isEmpty() || this.ingredients == null || this.ingredients.isEmpty() ||
                this.tags == null || this.tags.isEmpty();
    }
    /**
     * Увеличивает счётчик просмотров рецепта на один.
     */
    public void addView() {
        ++this.views;
    }
}
