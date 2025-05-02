package org.beautybox.service;

import jakarta.mail.MessagingException;
import org.beautybox.exception.BeautyBoxException;
import org.beautybox.request.ChangePasswordNoAuth;
import org.beautybox.request.LoginRequest;
import org.beautybox.response.TokenResponse;

import java.io.UnsupportedEncodingException;

public interface AuthenticationService {
    TokenResponse login(LoginRequest loginRequest);
    void logout(String token);
    void getOtp(String mail) throws BeautyBoxException, MessagingException, UnsupportedEncodingException;
    void verifyOtp(String mail, String otp);
    void changePasswordNoAuth(ChangePasswordNoAuth changePasswordNoAuth) throws BeautyBoxException;
}
