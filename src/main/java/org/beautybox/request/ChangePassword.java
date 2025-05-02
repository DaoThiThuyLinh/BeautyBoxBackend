package org.beautybox.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangePassword {
    @NotBlank(message = "Không được bỏ trống mật khẩu")
    String password;
    @NotBlank(message = "Mật khẩu xác nhận không để trống")
    String passwordConfirm;
}
