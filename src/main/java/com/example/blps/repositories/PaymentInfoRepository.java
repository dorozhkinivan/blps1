package com.example.blps.repositories;

import com.example.blps.entities.PaymentInfo;
import com.example.blps.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface PaymentInfoRepository extends JpaRepository<PaymentInfo, Long> {
    /**
     * Поиск платёжной информации по пользователю.
     * Этот метод возвращает платёжную информацию, связанную с указанным пользователем.
     * Используется для операций, связанных с платёжной информацией пользователя, таких как обновление данных о платеже.
     *
     * @param user Сущность пользователя, для которой требуется найти платёжную информацию.
     * @return Optional, содержащий платёжную информацию пользователя, если она существует,
     * или пустой Optional, если информация не найдена.
     */
    Optional<PaymentInfo> findByUser(User user);
}
