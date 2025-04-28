package org.beautybox.service;

import org.beautybox.request.UserRegisterRequest;
import org.beautybox.response.PageResponse;
import org.beautybox.response.UserDetailResponse;
import org.beautybox.response.UserResponse;

public interface UserService {
    void register(UserRegisterRequest registerRequest);
    UserResponse getUserByToken(String token);
    PageResponse<UserDetailResponse> getAllUser(String name, int pageIndex, int pageSize, String orderBy, String direction);
}
