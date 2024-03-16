package com.example.blps.services;


import com.example.blps.entities.PaymentInfo;
import com.example.blps.repositories.PaymentInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.blps.entities.User;
import com.example.blps.repositories.UserRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final PaymentInfoRepository paymentRepository;

    /**
     * Сохранение пользователя
     *
     * @return сохраненный пользователь
     */
    public User save(User user) {
        return repository.save(user);
    }

    /**
     * Сохранение платёжного аккаунта
     *
     * @return сохраненный платёжный аккаунт
     */
    public PaymentInfo savePaymentInfo(PaymentInfo paymentInfo) {
        return paymentRepository.save(paymentInfo);
    }

    /**
     * Создание пользователя с платёжным аккаунтом
     */
    @Transactional
    public void create(User user) {
        if (repository.existsByUsername(user.getUsername())) {
            // Заменить на свои исключения
            throw new RuntimeException("Пользователь с таким именем уже существует");
        }
        if (repository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Пользователь с таким email уже существует");
        }
        save(user);

        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setAmount(0.0);
        paymentInfo.setUser(user);
        paymentInfo.setWithdrawAccount("cardNo.");
        savePaymentInfo(paymentInfo);
    }

    /**
     * Получение пользователя по имени пользователя
     *
     * @return пользователь
     */
    public User getByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

    }

    /**
     * Получение пользователя по имени пользователя
     * <p>
     * Нужен для Spring Security
     *
     * @return пользователь
     */
    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    /**
     * Получение текущего пользователя
     *
     * @return текущий пользователь
     */
    public User getCurrentUser() {
        // Получение имени пользователя из контекста Spring Security
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }
}