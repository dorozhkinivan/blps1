package com.example.blps.controllers;

import com.example.blps.services.AdService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер для управления рекламными блоками.
 * Предоставляет API для получения статической рекламы.
 */
@RestController
@RequestMapping("ad")
@Tag(name = "Реклама")
@RequiredArgsConstructor
public class AdController {
    private final AdService adService;

    /**
     * Возвращает статическую рекламу для указанного поста.
     * Метод доступен для всех пользователей без ограничений.
     *
     * @param postId ID поста, для которого запрашивается реклама.
     * @return ResponseEntity с рекламным контентом или сообщением об ошибке.
     */
    @GetMapping("static")
    @Operation(summary = "Доступно всем пользователям")
    public ResponseEntity<?> showDraft(@RequestParam(value = "post_id") Long postId) {
        return adService.getStaticAd(postId);
    }
}
