package com.example.blps.controllers;

import com.example.blps.security.JwtAuthenticationResponse;
import com.example.blps.security.SignInRequest;
import com.example.blps.security.SignUpRequest;
import com.example.blps.services.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер для управления процессами аутентификации, включая регистрацию и вход в систему.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Аутентификация")
public class AuthController {
    private final AuthenticationService authenticationService;

    /**
     * Метод для регистрации новых пользователей.
     * Принимает данные пользователя, проводит их валидацию и, в случае успеха, создает новую учетную запись.
     *
     * @param request Тело запроса с данными для регистрации.
     * @return JwtAuthenticationResponse, содержащий JWT токен для авторизованного доступа.
     */
    @Operation(summary = "Регистрация пользователя")
    @PostMapping("/sign-up")
    public JwtAuthenticationResponse signUp(@RequestBody @Valid SignUpRequest request) {
        return authenticationService.signUp(request);
    }

    /**
     * Метод для входа в систему зарегистрированных пользователей.
     * Принимает учетные данные пользователя, проводит их валидацию и, в случае успеха, возвращает JWT токен.
     *
     * @param request Тело запроса с данными для входа в систему.
     * @return JwtAuthenticationResponse, содержащий JWT токен для авторизованного доступа.
     */
    @Operation(summary = "Авторизация пользователя")
    @PostMapping("/sign-in")
    public JwtAuthenticationResponse signIn(@RequestBody @Valid SignInRequest request) {
        return authenticationService.signIn(request);
    }
}