package com.example.blps.services;

import com.example.blps.entities.PaymentInfo;
import com.example.blps.entities.Recipe;
import com.example.blps.entities.User;
import com.example.blps.repositories.PaymentInfoRepository;
import com.example.blps.repositories.RecipeRepository;
import com.example.blps.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AdService {
    private final RecipeRepository recipeRepository;
    private final PaymentInfoRepository paymentInfoRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    /**
     * Получить статическую рекламу для заданного поста. В зависимости от наличия
     * и информации о пользователе, который разместил рецепт, метод возвращает различные виды рекламы.
     *
     * @param postId ID рецепта для которого запрашивается реклама.
     * @return ResponseEntity с текстом рекламы.
     */
    public ResponseEntity<?> getStaticAd(Long postId) {
        Optional<Recipe> recipe = recipeRepository.findById(postId);
        if (recipe.isEmpty()) {
            return ResponseEntity.ok("Неперсонализированная реклама макдоналдс");
        }
        Optional<User> user = userRepository.findById(recipe.get().getOwnerId());
        if (user.isEmpty()) {
            return ResponseEntity.ok("Неперсонализированная реклама шавермы");
        }
        Optional<PaymentInfo> paymentInfo = paymentInfoRepository.findByUser(user.get());
        if (paymentInfo.isEmpty()) {
            return ResponseEntity.ok("Неперсонализированная реклама шавермы Шэра");
        }
        PaymentInfo newPaymentInfo = paymentInfo.get();
        newPaymentInfo.setAmount(newPaymentInfo.getAmount() + 1);
        // Сохраняем обновленную информацию о платежах
        userService.savePaymentInfo(newPaymentInfo);

        return ResponseEntity.ok("Купите франшизу макдоналдс :(");
    }
}
