package org.beautybox.service.impl;

import io.jsonwebtoken.Claims;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.beautybox.entity.User;
import org.beautybox.exception.BeautyBoxException;
import org.beautybox.exception.ErrorDetail;
import org.beautybox.repository.RedisRepository;
import org.beautybox.repository.UserRepository;
import org.beautybox.request.LoginRequest;
import org.beautybox.response.TokenResponse;
import org.beautybox.service.AuthenticationService;
import org.beautybox.service.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationServiceImpl implements AuthenticationService {

    UserRepository userRepository;
    AuthenticationManager authenticationManager;
    JwtService jwtService;
    RedisRepository redisRepository;

    @Override
    @SneakyThrows
    public TokenResponse login(LoginRequest loginRequest) {
        User user = userRepository.findUserByEmail(loginRequest.getEmail());
        if (user == null)
        {
            throw new BeautyBoxException(ErrorDetail.ERR_USER_UN_AUTHENTICATE);
        }

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
            ));
        }catch (Exception e){
            throw new BeautyBoxException(ErrorDetail.ERR_USER_UN_AUTHENTICATE);
        }
        final long TIME_TOKEN = 1000L * 60 * 60 * 24;
        var tokenContent = jwtService.generateToken(user, TIME_TOKEN);
        var refreshToken = jwtService.generateToken(user, TIME_TOKEN * 2);

        return TokenResponse.builder()
                .tokenContent(tokenContent)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .userName(user.getEmail())
                .roleName(user.getRole().getName())
                .expToken(new Timestamp(System.currentTimeMillis() + TIME_TOKEN))
                .expRefreshToken(new Timestamp(System.currentTimeMillis() + TIME_TOKEN * 2))
                .build();
    }

    @Override
    public void logout(String token) {
        Date date = jwtService.extractClaim(token, Claims::getExpiration);
        redisRepository.set(token, jwtService.extractUsername(token));
        redisRepository.setTimeToLive(token, date.getTime() - new Date().getTime());
        System.out.println("Token date: " + date);
    }
}
