package com.example.blps.services;

import com.example.blps.entities.Recipe;
import com.example.blps.entities.Review;
import com.example.blps.entities.User;
import com.example.blps.model.ModerationResultDTO;
import com.example.blps.model.ReviewDTO;
import com.example.blps.repositories.RecipeRepository;
import com.example.blps.repositories.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final ReviewRepository reviewRepository;

    /**
     * Показать черновик рецепта по ID, если пользователь является владельцем.
     *
     * @param id          ID рецепта.
     * @param currentUser Текущий пользователь, сделавший запрос.
     * @return ResponseEntity с рецептом или сообщением об ошибке.
     */
    public ResponseEntity<?> showDraft(Long id, User currentUser) {
        Recipe recipe = recipeRepository.findById(id).orElse(null);
        if (recipe == null) {
            return ResponseEntity.notFound().build();
        }
        if (!Objects.equals(recipe.getOwnerId(), currentUser.getId())) {
            return new ResponseEntity<>("Доступ запрещен", HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok(recipe);
    }

    /**
     * Показать рецепт по ID, если рецепт опубликован и прошел модерацию.
     *
     * @param id ID рецепта.
     * @return ResponseEntity с рецептом или сообщением об ошибке.
     */
    public ResponseEntity<?> show(Long id) {
        Optional<Recipe> recipeOpt = recipeRepository.findById(id);
        if (recipeOpt.isPresent()) {
            Recipe recipe = recipeOpt.get();
            boolean isDraft = recipe.getIsDraft();
            boolean moderStatus = recipe.getModerStatus();
            if (isDraft || moderStatus) {
                return new ResponseEntity<>("а вот нельзя. рецепт ещё не готов", HttpStatus.CREATED);
            } else {
                recipe.addView();
                recipeRepository.save(recipe);
                return ResponseEntity.ok(recipe);
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Показать все рецепты на модерации.
     *
     * @return ResponseEntity со списком рецептов.
     */
    public ResponseEntity<?> showModeration() {
        Optional<List<Recipe>> recipeOptional = recipeRepository.findByModerStatus(true);
        return ResponseEntity.ok(recipeOptional);
    }

    /**
     * Отправить результат модерации рецепта.
     *
     * @param moderationResult Результат модерации.
     * @return ResponseEntity с сообщением об обновлении статуса.
     */
    public ResponseEntity<?> sendModerationResult(ModerationResultDTO moderationResult) {
        Recipe recipe = recipeRepository.findById(moderationResult.getId()).orElse(null);
        if (recipe == null) {
            return ResponseEntity.notFound().build();
        }
        recipe.setModerStatus(false);
        recipe.setModerComment(moderationResult.getModerComment());
        recipe.setIsDraft(!moderationResult.getModerResult());
        recipeRepository.save(recipe);
        return ResponseEntity.ok("Статус обновлён");
    }

    /**
     * Получить отзывы к рецепту по ID.
     *
     * @param id ID рецепта.
     * @return ResponseEntity со списком отзывов.
     */
    public ResponseEntity<Optional<List<Review>>> reviews(Long id) {
        Optional<List<Review>> reviews = reviewRepository.findByEntityId(id);
        return ResponseEntity.ok(reviews);
    }

    /**
     * Проверяет доступ пользователя к рецепту.
     * Этот метод предназначен для внутреннего использования сервисом для проверки,
     * имеет ли текущий пользователь право на модификацию заданного рецепта.
     *
     * @param recipe      Рецепт, к которому осуществляется доступ.
     * @param currentUser Пользователь, осуществляющий доступ.
     * @return Строковый идентификатор результата проверки ("true", "not found", "Доступ запрещен").
     */
    private String checkAccess(Recipe recipe, User currentUser) {
        if (recipe.getId() != null) {
            Recipe oldRecipe = recipeRepository.findById(recipe.getId()).orElse(null);
            if (oldRecipe == null) {
                return "not found";
            }
            if (!Objects.equals(oldRecipe.getOwnerId(), currentUser.getId())) {
                return "Доступ запрещен";
            }
        }
        recipe.setOwnerId(currentUser.getId());
        recipe.setViews(0);
        return "true";
    }

    /**
     * Добавляет новый черновик рецепта.
     *
     * @param recipe      Данные рецепта для сохранения в виде черновика.
     * @param currentUser Текущий пользователь, создающий черновик.
     * @return ResponseEntity с созданным черновиком или сообщением об ошибке.
     */
    public ResponseEntity<?> addDraft(Recipe recipe, User currentUser) {
        String response = checkAccess(recipe, currentUser);
        if (response.equals("not found")) {
            return ResponseEntity.notFound().build();
        } else if (response.equals("Доступ запрещен")) {
            return new ResponseEntity<>("Доступ запрещен", HttpStatus.FORBIDDEN);
        }
        recipe.setIsDraft(true);
        recipe.setModerStatus(false);
        Recipe savedDraft = recipeRepository.save(recipe);
        return new ResponseEntity<>(savedDraft, HttpStatus.CREATED);
    }

    /**
     * Добавляет новый рецепт или обновляет существующий.
     * Если рецепт содержит недостаточно данных для публикации, он сохраняется как черновик.
     *
     * @param recipe      Данные рецепта для добавления или обновления.
     * @param currentUser Текущий пользователь, добавляющий или обновляющий рецепт.
     * @return ResponseEntity с добавленным или обновленным рецептом или сообщением об ошибке.
     */
    public ResponseEntity<?> add(Recipe recipe, User currentUser) {
        String response = checkAccess(recipe, currentUser);
        if (response.equals("not found")) {
            return ResponseEntity.notFound().build();
        } else if (response.equals("Доступ запрещен")) {
            return new ResponseEntity<>("Доступ запрещен", HttpStatus.FORBIDDEN);
        }
        boolean isDraft = recipe.hasNullFieldsForDraft();
        recipe.setIsDraft(isDraft);
        if (!recipe.getIsDraft()) {
            recipe.setModerStatus(true);
        }
        Recipe savedRecipe = recipeRepository.save(recipe);
        return new ResponseEntity<>(savedRecipe, HttpStatus.CREATED);
    }

    /**
     * Добавляет отзыв к рецепту.
     *
     * @param reviewDTO   Данные отзыва, включая ID рецепта, текст отзыва и оценку.
     * @param currentUser Пользователь, оставляющий отзыв.
     * @return ResponseEntity с сохраненным отзывом или сообщением об ошибке.
     */
    public ResponseEntity<?> addReview(ReviewDTO reviewDTO, User currentUser) {
        Review review = new Review();
        if (reviewDTO.hasNulls()) {
            return new ResponseEntity<>("Пропущены данные в теле запроса", HttpStatus.BAD_REQUEST);
        }
        review.setText(reviewDTO.getComment());
        review.setRating(reviewDTO.getRating());
        review.setEntityId(reviewDTO.getRecipeId());
        review.setUserId(currentUser.getId());
        Review savedReview = reviewRepository.save(review);
        return new ResponseEntity<>(savedReview, HttpStatus.CREATED);
    }
}
