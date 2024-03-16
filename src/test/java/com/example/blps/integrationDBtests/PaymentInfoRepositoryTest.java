package com.example.blps.integrationDBtests;

import com.example.blps.entities.PaymentInfo;
import com.example.blps.entities.User;
import com.example.blps.repositories.PaymentInfoRepository;
import com.example.blps.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Testcontainers
public class PaymentInfoRepositoryTest extends AbstractIntegrationTest {
    @Autowired
    private PaymentInfoRepository repo;
    @Autowired
    private UserRepository UserRepo;

    @Test
    @Transactional
    public void findByUserTest() {
        PaymentInfo basePaymentInfo = insertPayment();
        Optional<PaymentInfo> resp = repo.findByUser(basePaymentInfo.getUser());
        assertThat(resp).isPresent();
        PaymentInfo paymentInfo = resp.get();
        assertThat(paymentInfo.getUser()).isEqualTo(basePaymentInfo.getUser());
    }

    private PaymentInfo insertPayment() {
        User user = User.builder()
                .username("testUser")
                .email("test@example.com")
                .password("password")
                .build();
        UserRepo.save(user);
        UserRepo.flush();
        PaymentInfo paymentInfo = PaymentInfo.builder()
                .user(user)
                .amount(22.2)
                .withdrawAccount("data")
                .build();
        repo.save(paymentInfo);
        repo.flush();
        return paymentInfo;
    }
}
