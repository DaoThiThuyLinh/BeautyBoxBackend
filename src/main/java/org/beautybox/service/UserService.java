package org.beautybox.service;

import org.beautybox.entity.User;
import org.beautybox.exception.BeautyBoxException;
import org.beautybox.request.UpdateUserRequest;
import org.beautybox.request.UserRegisterRequest;
import org.beautybox.response.PageResponse;
import org.beautybox.response.UserDetailResponse;
import org.beautybox.response.UserResponse;

public interface UserService {
    void register(UserRegisterRequest registerRequest);
    void update(UpdateUserRequest updateUserRequest, User user) throws BeautyBoxException;
    void delete(String userId) throws BeautyBoxException;
    UserResponse getUserByToken(String token);
    PageResponse<UserDetailResponse> getAllUser(String name, int pageIndex, int pageSize, String orderBy, String direction);
}
