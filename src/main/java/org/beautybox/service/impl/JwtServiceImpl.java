package org.beautybox.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.beautybox.service.JwtService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {

    private static final String SECRET_KEY = "OugG/owlbJvsUvfrTo6pBHvoCse7tEVtBlEMQ6JFywtOIaUsWRgPrRSd9yY8HjcO";

    @Override
    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    @Override
    public String generateToken(UserDetails userDetails, Long time){
        return generateToken(new HashMap<>(),userDetails, time);
    }

    @Override
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails, Long time){
        return Jwts
                .builder()
                .subject(userDetails.getUsername())
                .claims(extraClaims)
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime()  + time))
                .signWith(getSignInKey())
                .compact();
    }


    @Override
    public Long getExpirationToken(String token){
        return this.extractExpiration(token).getTime() - extractClaim(token, Claims::getIssuedAt).getTime();
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    @Override
    public  boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token){
        return extractClaim(token,Claims::getExpiration);
    }

    private Claims extractAllClaims(String token){
        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSignInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
