package org.beautybox.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.beautybox.request.LoginRequest;
import org.beautybox.request.UserRegisterRequest;
import org.beautybox.response.ApiResponse;
import org.beautybox.response.TokenResponse;
import org.beautybox.response.UserResponse;
import org.beautybox.service.AuthenticationService;
import org.beautybox.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    UserService userService;
    AuthenticationService authenticationService;

    @Operation(summary = "Đăng kí người dùng mới")
    @PostMapping("/public-api/register")
    public ApiResponse register(@RequestBody @Valid UserRegisterRequest userRegisterRequest) {
        userService.register(userRegisterRequest);
        return ApiResponse.builder()
                .code(200)
                .message("Register success")
                .build();
    }

    @Operation(summary = "Đăng nhập")
    @PostMapping("/public-api/login")
    public ApiResponse login(@RequestBody @Valid LoginRequest loginRequest) {
        TokenResponse tokenResponse = authenticationService.login(loginRequest);
        return ApiResponse.builder()
                .code(200)
                .message("Login success")
                .data(tokenResponse)
                .build();
    }

    @Operation(summary = "Đăng xuất", security = {
            @SecurityRequirement(name = "bearerAuth")
    })
    @PostMapping("/sign-out")
    public ApiResponse logout(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION).substring(7);
        authenticationService.logout(token);
        return ApiResponse.builder()
                .code(200)
                .message("Logout success")
                .build();
    }


    @Operation(summary = "Lấy thông tin tài khoản bẳng token", security = {
            @SecurityRequirement(name = "bearerAuth")
    })
    @GetMapping(value = "/user")
    public ApiResponse getUser(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION).substring(7);
        System.out.println(token);
        UserResponse userResponse = userService.getUserByToken(token);
        return ApiResponse.builder()
                .code(200)
                .data(userResponse)
                .build();
    }
}
