package org.beautybox.service.impl;

import io.jsonwebtoken.Claims;
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.beautybox.entity.User;
import org.beautybox.exception.BeautyBoxException;
import org.beautybox.exception.ErrorDetail;
import org.beautybox.repository.RedisRepository;
import org.beautybox.repository.UserRepository;
import org.beautybox.request.ChangePasswordNoAuth;
import org.beautybox.request.LoginRequest;
import org.beautybox.response.TokenResponse;
import org.beautybox.service.AuthenticationService;
import org.beautybox.service.JwtService;
import org.beautybox.service.MailService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Random;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationServiceImpl implements AuthenticationService {

    UserRepository userRepository;
    AuthenticationManager authenticationManager;
    JwtService jwtService;
    RedisRepository redisRepository;
    MailService mailService;
    SpringTemplateEngine templateEngine;
    PasswordEncoder passwordEncoder;

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

    @Override
    public void getOtp(String mail) throws BeautyBoxException, MessagingException, UnsupportedEncodingException {
        User user= userRepository.findUserByEmail(mail);
        if(user==null){
            throw new BeautyBoxException(ErrorDetail.ERR_USER_NOT_EXISTED);
        }
        Random random= new Random();
        int code= random.nextInt(100000, 999999);

        Context context= new Context();
        context.setVariable("code", code);
        context.setVariable("name", user.getName());
        String content= templateEngine.process("mail-otp", context);
        String subject= "Mã xác thực tài khoản";
        mailService.sendMail(subject,mail, content, true);
        redisRepository.set(user.getEmail(), code);
        redisRepository.setTimeToLive(user.getEmail(), 90L * 1000);
    }

    @Override
    public void verifyOtp(String mail, String otp) {
        Object code = redisRepository.get(mail);
        if(code == null){
            throw new RuntimeException("OTP không chính xác");
        }
        if(otp == null || !otp.equals(code.toString())){
            throw new RuntimeException("OTP không chính xác");
        }
        redisRepository.set(mail, "verified");
        redisRepository.setTimeToLive(mail, 5 * 60L * 1000);
    }

    @Override
    public void changePasswordNoAuth(ChangePasswordNoAuth changePasswordNoAuth) throws BeautyBoxException {
        Object auth = redisRepository.get(changePasswordNoAuth.getMail());
        if(auth == null){
            throw new RuntimeException("Thay đổi mật khẩu chỉ diễn ra trong 5 phút sau khi xác thực OTP, vui lòng xác thực lại OTP");
        }else{
            if(!"verified".equals(redisRepository.get(changePasswordNoAuth.getMail()).toString())){
                throw new RuntimeException("Bạn chưa xác thực OTP");
            }
        }
        User user= userRepository.findUserByEmail(changePasswordNoAuth.getMail());
        if(user==null){
            throw new BeautyBoxException(ErrorDetail.ERR_USER_NOT_EXISTED);
        }
        if(!changePasswordNoAuth.getPassword().equals(changePasswordNoAuth.getPasswordConfirm())){
            throw new BeautyBoxException(ErrorDetail.ERR_PASSWORD_CONFIRM_INCORRECT);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        redisRepository.set(changePasswordNoAuth.getMail(), null);
    }
}
