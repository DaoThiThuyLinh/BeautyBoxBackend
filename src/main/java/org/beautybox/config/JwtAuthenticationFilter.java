package org.beautybox.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.beautybox.repository.RedisRepository;
import org.beautybox.response.ApiResponse;
import org.beautybox.service.JwtService;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    final RedisRepository redisRepository;
    final JwtService jwtService;
    final UserDetailsService userDetailsService;
    final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String jwt;
        final String username;

        String clientIP = request.getRemoteAddr();
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            clientIP = xForwardedFor.split(",")[0].trim();
        }
        String contentLog= "IP: " + clientIP + " WORK WITH API: '" + request.getRequestURI() + "' WITH METHOD: " + request.getMethod();
        log.info(contentLog);

        if (authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }
        jwt = authHeader.substring(7);
        Object a = redisRepository.get(jwt);
        if(a != null) {
            response.setContentType("application/json; charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            ApiResponse apiResponse= ApiResponse.error(401, "Phiên đã hết hạn, vui lòng đăng nhập lại !");
            String content= objectMapper.writeValueAsString(apiResponse);
            response.getWriter().write(content);
            return;
        }
        try {
            username = jwtService.extractUsername(jwt);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            response.setContentType("application/json; charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            ApiResponse apiResponse= ApiResponse.error(401, "Phiên đã hết hạn, vui lòng đăng nhập lại !");
            String content= objectMapper.writeValueAsString(apiResponse);
            response.getWriter().write(content);
        }
    }
}
