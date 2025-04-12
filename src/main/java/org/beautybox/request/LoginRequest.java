package org.beautybox.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginRequest {
    @NotBlank(message = "Email is required")
    String email;
    @NotBlank(message = "Password is required")
    String password;
}
