package org.beautybox.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurity {

    final JwtAuthenticationFilter jwtAuthenticationFilter;

    final String[] listUnAuthenticate={
            "/v3/api-docs/**"
            , "/swagger-ui/**"
            , "/swagger-ui.html"
    };
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(httpSecurityCorsConfigurer -> corsFilter())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        t -> t.requestMatchers("/public-api/**").permitAll()
                                .requestMatchers(listUnAuthenticate).permitAll()
                                .requestMatchers("/admin-api/**").hasAuthority("ROLE_ADMIN")
                                .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter , UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

    @Bean
    CorsFilter corsFilter(){
        CorsConfiguration corsConfiguration = new CorsConfiguration() ;
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource() ;
        source.registerCorsConfiguration("/**" , corsConfiguration);
        return new CorsFilter(source) ;
    }
}
