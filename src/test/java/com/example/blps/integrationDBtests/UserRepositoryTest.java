package com.example.blps.integrationDBtests;

import com.example.blps.entities.User;
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
public class UserRepositoryTest extends AbstractIntegrationTest {
    @Autowired
    private UserRepository repo;

    @Test
    @Transactional
    public void findByIdTest(){
        User baseUser = insertUsers();
        Optional<User> resp = repo.findById(baseUser.getId());
        assertThat(resp).isPresent();
        User user = resp.get();
        assertThat(user.getUsername()).isEqualTo(baseUser.getUsername());
    }

    @Test
    @Transactional
    public void findByUsernameTest(){
        User baseUser = insertUsers();
        Optional<User> resp = repo.findByUsername(baseUser.getUsername());
        assertThat(resp).isPresent();
        User user = resp.get();
        assertThat(user.getId()).isEqualTo(baseUser.getId());
    }

    @Test
    @Transactional
    public void existsByUsernameTest(){
        User baseUser = insertUsers();
        boolean trueResp = repo.existsByUsername(baseUser.getUsername());
        boolean falseResp = repo.existsByUsername(baseUser.getUsername() + '1');
        assertThat(trueResp).isTrue();
        assertThat(falseResp).isFalse();
    }

    @Test
    @Transactional
    public void existsByEmailTest(){
        User baseUser = insertUsers();
        boolean trueResp = repo.existsByEmail(baseUser.getEmail());
        boolean falseResp = repo.existsByEmail(baseUser.getEmail() + '1');
        assertThat(trueResp).isTrue();
        assertThat(falseResp).isFalse();
    }

    private User insertUsers() {
        User user = User.builder()
                .username("testUser")
                .email("test@example.com")
                .password("password")
                .build();
        repo.save(user);
        repo.flush();
        return user;
    }

}
