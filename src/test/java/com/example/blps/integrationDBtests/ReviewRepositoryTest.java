package com.example.blps.integrationDBtests;

import com.example.blps.entities.*;
import com.example.blps.repositories.ReviewRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Testcontainers
public class ReviewRepositoryTest extends AbstractIntegrationTest {
    @Autowired
    private ReviewRepository repo;

    @Test
    @Transactional
    public void findByIdTest() {
        Review baseReview = insertUsers();
        Optional<Review> resp = repo.findById(baseReview.getId());
        assertThat(resp).isPresent();
        Review user = resp.get();
        assertThat(user.getText()).isEqualTo(baseReview.getText());
    }

    @Test
    @Transactional
    public void findByEntityIdTest() {
        Review baseReview = insertUsers();
        Optional<List<Review>> resp = repo.findByEntityId(baseReview.getEntityId());
        assertThat(resp).isPresent();
        List<Review> reviews = resp.get();
        assertThat(reviews).isNotEmpty();
        assertThat(reviews.getFirst().getText()).isEqualTo(baseReview.getText());
    }

    private Review insertUsers() {
        Review review = Review.builder()
                .text("хороший рецепт")
                .rating(5)
                .userId(1L)
                .entityId(2L)
                .build();
        repo.save(review);
        repo.flush();
        return review;
    }

}
