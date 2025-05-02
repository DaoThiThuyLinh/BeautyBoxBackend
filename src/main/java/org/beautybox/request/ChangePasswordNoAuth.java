package org.beautybox.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangePasswordNoAuth {
    @NotBlank(message = "Không bỏ trống mail")
    String mail;
    @NotBlank(message = "Không được bỏ trống mật khẩu")
    String password;
    @NotBlank(message = "Mật khẩu xác nhận không để trống")
    String passwordConfirm;
}
