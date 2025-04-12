package org.beautybox.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.beautybox.entity.User;
import org.beautybox.exception.BeautyBoxException;
import org.beautybox.exception.ErrorDetail;
import org.beautybox.mapper.UserMapper;
import org.beautybox.repository.RoleRepository;
import org.beautybox.repository.UserRepository;
import org.beautybox.request.UserRegisterRequest;
import org.beautybox.response.UserResponse;
import org.beautybox.service.JwtService;
import org.beautybox.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService, UserDetailsService {

    UserRepository userRepository;
    RoleRepository roleRepository;
    JwtService jwtService;
    PasswordEncoder passwordEncoder;
    UserMapper userMapper;

    @SneakyThrows
    @Override
    public boolean register(UserRegisterRequest registerRequest) {
        if(!registerRequest.getPassword().equals(registerRequest.getPasswordConfirm())){
            throw new BeautyBoxException(ErrorDetail.ERR_PASSWORD_CONFIRM_INCORRECT);
        }
        if(userRepository.existsByEmail(registerRequest.getEmail())){
            throw new BeautyBoxException(ErrorDetail.ERR_USER_EMAIL_EXISTED);
        }
        User user =  userMapper.fromRegisterRequest(registerRequest);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(roleRepository.findByName("ROLE_USER"));
        userRepository.save(user);
        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    @Override
    public UserResponse getUserByToken(String token) {
        String username = jwtService.extractUsername(token);
        User user = userRepository.findUserByEmail(username);
        return userMapper.toResponse(user);
    }
}
