package org.beautybox.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.beautybox.anotation.CurrentUser;
import org.beautybox.entity.User;
import org.beautybox.exception.BeautyBoxException;
import org.beautybox.request.*;
import org.beautybox.response.ApiResponse;
import org.beautybox.response.TokenResponse;
import org.beautybox.response.UserResponse;
import org.beautybox.service.AuthenticationService;
import org.beautybox.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

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

    @PutMapping("/user")
    public ApiResponse update(@RequestBody @Valid UpdateUserRequest updateRequest, @CurrentUser User user) throws BeautyBoxException {
        userService.update(updateRequest, user);
        return ApiResponse.success("Cập nhập thành công");
    }

    @DeleteMapping("/admin-api/user/{userId}")
    public ApiResponse delete(@PathVariable String userId) throws BeautyBoxException {
        userService.delete(userId);
        return ApiResponse.success("Xoá thành công");
    }


    @Operation(summary = "Lấy ra danh sách user", parameters = {
            @Parameter(name = "orderBy", description = "<h4>Truyền vào giá trị từ 1->4</h4>" +
                    "{1}. Sắp xếp theo thời gian tạo (Mặc định) </br>" +
                    "{2}. Sắp xếp theo số lượng đơn hàng </br>" +
                    "{3}. Sắp xếp theo tên </br>" +
                    "{4}. Sắp xếp theo tổng tiền mua hàng </br>")
    })
    @GetMapping("/admin-api/user")
    public ApiResponse getAllUser(@RequestParam(required = false, defaultValue = "") String value,
                                  @RequestParam(required = false, defaultValue = "1") int pageIndex,
                                  @RequestParam(required = false, defaultValue = "40") int pageSize,
                                  @RequestParam(required = false, defaultValue = "1") String orderBy,
                                  @RequestParam(required = false, defaultValue = "asc") String sortDirection){
        return ApiResponse.success("Danh sách người dùng", userService.getAllUser(value, pageIndex, pageSize, orderBy, sortDirection));
    }


    @Operation(summary = "OTP tồn tại trong 90s")
    @PostMapping("/public-api/get-otp")
    public ApiResponse getOtp(@RequestParam String mail) throws BeautyBoxException, MessagingException, UnsupportedEncodingException {
        authenticationService.getOtp(mail);
        return ApiResponse.success("Mã xác nhận đã được gửi qua mail");
    }

    @Operation(summary = "Sau khi xác thực, thao tác đổi mật khẩu chỉ diễn ra trong 5p")
    @PostMapping("/public-api/verify-otp")
    public ApiResponse verifyOtp(@RequestParam String mail,
                                 @RequestParam String code) {
        authenticationService.verifyOtp(mail, code);
        return ApiResponse.success("Xác thực OTP thành công");
    }

    @Operation(summary = "Thay đổi mật khẩu trong thao tác quên mật khẩu")
    @PostMapping("/public-api/change-password")
    public ApiResponse changePasswordNoAuth(@RequestBody @Valid ChangePasswordNoAuth request) throws BeautyBoxException {
        authenticationService.changePasswordNoAuth(request);
        return ApiResponse.success("Thay đổi mật khẩu thành công");
    }

    @Operation(summary = "Thay đổi mật khẩu bình thường")
    @PostMapping("/change-password")
    public ApiResponse changePassword(@RequestBody @Valid ChangePassword request, @CurrentUser User user) throws BeautyBoxException {
        authenticationService.changePassword(request, user);
        return ApiResponse.success("Đổi mật khẩu thành công");
    }
}
