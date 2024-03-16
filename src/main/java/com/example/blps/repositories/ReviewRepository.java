package com.example.blps.repositories;

import com.example.blps.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    /**
     * Находит список отзывов по идентификатору сущности.
     * Этот метод предназначен для извлечения всех отзывов, связанных с определенной сущностью,
     * такой как рецепт, по его уникальному идентификатору.
     *
     * @param entityId Идентификатор сущности, для которой необходимо найти отзывы.
     * @return Optional список отзывов, связанных с указанной сущностью. Возвращает пустой Optional,
     * если отзывы не найдены.
     */
    Optional<List<Review>> findByEntityId(Long entityId);
}
