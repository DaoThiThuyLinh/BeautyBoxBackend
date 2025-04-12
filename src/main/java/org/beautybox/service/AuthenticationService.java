package org.beautybox.service;

import org.beautybox.request.LoginRequest;
import org.beautybox.response.TokenResponse;

public interface AuthenticationService {
    TokenResponse login(LoginRequest loginRequest);
    void logout(String token);
}
