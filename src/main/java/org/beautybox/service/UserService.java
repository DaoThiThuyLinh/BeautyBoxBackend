package org.beautybox.service;

import org.beautybox.request.UserRegisterRequest;
import org.beautybox.response.UserResponse;

public interface UserService {
    boolean register(UserRegisterRequest registerRequest);
    UserResponse getUserByToken(String token);
}
