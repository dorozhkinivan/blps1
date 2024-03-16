package com.example.blps.entities;

import jakarta.persistence.*;
import lombok.*;

/**
 * Класс сущности, представляющий платёжную информацию пользователя.
 */
@Entity
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payment_info")
public class PaymentInfo {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(name = "amount", nullable = false)
    private Double amount;
    @Column(name = "withdraw_account", nullable = false)
    private String withdrawAccount;
}
