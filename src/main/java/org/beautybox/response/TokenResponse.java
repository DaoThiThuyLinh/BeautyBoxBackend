package org.beautybox.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;

@Builder
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TokenResponse {
    String tokenContent ;
    String refreshToken ;
    String userId ;
    String userName ;
    String roleName ;
    Timestamp expToken ;
    Timestamp expRefreshToken ;
}