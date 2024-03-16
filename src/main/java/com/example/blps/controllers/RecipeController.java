package com.example.blps.controllers;

import com.example.blps.entities.Recipe;
import com.example.blps.entities.Review;
import com.example.blps.entities.User;
import com.example.blps.model.ModerationResultDTO;
import com.example.blps.model.ReviewDTO;
import com.example.blps.services.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Контроллер для управления рецептами, включая просмотр, добавление и модерацию.
 */
@RestController
@RequestMapping("recipe")
@Tag(name = "Рецепты")
@RequiredArgsConstructor
public class RecipeController {
    private final RecipeService recipeService;

    /**
     * Показывает черновик рецепта, если текущий пользователь является его владельцем.
     *
     * @param id ID рецепта.
     * @return ResponseEntity с рецептом или сообщением об ошибке.
     */
    @GetMapping("show/draft")
    @Operation(summary = "Доступен только авторизованным пользователям")
    public ResponseEntity<?> showDraft(@RequestParam(value = "id") Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        return recipeService.showDraft(id, currentUser);
    }

    /**
     * Показывает рецепт всем пользователям.
     *
     * @param id ID рецепта.
     * @return ResponseEntity с рецептом или сообщением об ошибке.
     */
    @GetMapping("show")
    @Operation(summary = "Доступен всем пользователям")
    public ResponseEntity<?> show(@RequestParam(value = "id") Long id) {
        return recipeService.show(id);
    }

    /**
     * Показывает рецепты, ожидающие модерации.
     *
     * @return ResponseEntity со списком рецептов на модерации.
     */
    @GetMapping("moderation/show")
    @Operation(summary = "Доступен только авторизованным пользователям (потом авторизованным модераторам)")
    public ResponseEntity<?> showModeration() {
        return recipeService.showModeration();
    }

    /**
     * Принимает результат модерации рецепта.
     *
     * @param moderationResult Результат модерации рецепта.
     * @return ResponseEntity с сообщением об обновлении статуса рецепта.
     */
    @PostMapping("moderation/send_result")
    @Operation(summary = "Доступен только авторизованным пользователям")
    public ResponseEntity<?> sendModerationResult(@RequestBody ModerationResultDTO moderationResult) {
        return recipeService.sendModerationResult(moderationResult);
    }

    /**
     * Возвращает отзывы к рецепту.
     *
     * @param id ID рецепта.
     * @return ResponseEntity со списком отзывов.
     */
    @GetMapping("reviews")
    @Operation(summary = "Доступен всем пользователям")
    public ResponseEntity<Optional<List<Review>>> reviews(@RequestParam(value = "id") Long id) {
        return recipeService.reviews(id);
    }

    /**
     * Добавляет черновик рецепта.
     *
     * @param recipe Объект рецепта для добавления.
     * @return ResponseEntity с добавленным черновиком рецепта.
     */
    @PostMapping("add/draft")
    @Operation(summary = "Доступен только авторизованным пользователям")
    public ResponseEntity<?> addDraft(@RequestBody Recipe recipe) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        return recipeService.addDraft(recipe, currentUser);
    }

    /**
     * Добавляет рецепт.
     *
     * @param recipe Объект рецепта для добавления.
     * @return ResponseEntity с добавленным рецептом.
     */
    @PostMapping("add")
    @Operation(summary = "Доступен только авторизованным пользователям")
    public ResponseEntity<?> add(@RequestBody Recipe recipe) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        return recipeService.add(recipe, currentUser);
    }

    /**
     * Добавляет отзыв к рецепту.
     *
     * @param review DTO отзыва.
     * @return ResponseEntity с добавленным отзывом.
     */
    @PostMapping("add/review")
    @Operation(summary = "Доступен только авторизованным пользователям")
    public ResponseEntity<?> addReview(@RequestBody ReviewDTO review) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        return recipeService.addReview(review, currentUser);
    }
}
