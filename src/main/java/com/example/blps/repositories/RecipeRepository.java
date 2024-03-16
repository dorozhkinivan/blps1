package com.example.blps.repositories;

import com.example.blps.entities.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    /**
     * Находит рецепты по статусу модерации.
     * Этот метод позволяет извлекать все рецепты, которые имеют указанный статус модерации,
     * что может быть полезно для реализации функциональности модерации контента.
     *
     * @param moderStatus Булево значение, указывающее статус модерации рецептов, которые нужно найти.
     *                    True, если рецепт ожидает модерации, и false в противном случае.
     * @return Optional список рецептов с заданным статусом модерации. Возвращает пустой Optional,
     * если рецепты с указанным статусом модерации не найдены.
     */
    Optional<List<Recipe>> findByModerStatus(boolean moderStatus);
}

