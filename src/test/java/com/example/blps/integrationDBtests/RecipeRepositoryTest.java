package com.example.blps.integrationDBtests;

import com.example.blps.entities.Ingredient;
import com.example.blps.entities.Recipe;
import com.example.blps.entities.RecipeStep;
import com.example.blps.repositories.RecipeRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Testcontainers
public class RecipeRepositoryTest extends AbstractIntegrationTest {
    @Autowired
    private RecipeRepository repo;

    @Test
    @Transactional
    public void findByIdTest(){
        Recipe baseRecipe = insertRecipes();
        Optional<Recipe> resp = repo.findById(baseRecipe.getId());
        assertThat(resp).isPresent();
        Recipe recipe = resp.get();
        assertThat(recipe.getTimeToCook()).isEqualTo(baseRecipe.getTimeToCook());
    }

    @Test
    @Transactional
    public void findByModerStatusTest(){
        Recipe baseRecipe = insertRecipes();
        Optional<List<Recipe>> resp = repo.findByModerStatus(true);
        assertThat(resp).isPresent();
        List<Recipe> recipes = resp.get();
        assertThat(recipes).isNotEmpty();
        assertThat(recipes.getFirst().getTimeToCook()).isEqualTo(baseRecipe.getTimeToCook());
    }

    private Recipe insertRecipes() {
        Ingredient flour = Ingredient.builder()
                .name("Flour")
                .weight(500.0)
                .quantity(1)
                .build();
        Ingredient water = Ingredient.builder()
                .name("Water")
                .weight(300.0)
                .quantity(1)
                .build();
        RecipeStep step1 = RecipeStep.builder()
                .stepText("Mix flour and water until smooth.")
                .stepPhotos(Arrays.asList("step1photo1.jpg", "step1photo2.jpg"))
                .build();
        Recipe pizzaRecipe = Recipe.builder()
                .ownerId(1L)
                .steps(Collections.singletonList(step1))
                .mainPhotoUrl("pizzaMainPhoto.jpg")
                .tags(Arrays.asList("Italian", "Main Course"))
                .category("Pizza")
                .timeToCook(45)
                .numberOfServings(4)
                .ingredients(Arrays.asList(flour, water))
                .moderComment("Needs more photos.")
                .moderStatus(true)
                .isDraft(false)
                .views(0)
                .build();
        repo.save(pizzaRecipe);
        repo.flush();
        return pizzaRecipe;
    }

}
