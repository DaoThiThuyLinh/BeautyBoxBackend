package org.beautybox.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRegisterRequest{
    @NotBlank(message = "Name field cannot be left empty")
            @Size(min = 2, message = "Name field must have at least 2 characters")
    String name;
    @NotBlank(message = "Password field cannot be left empty")
    String password;
    @NotBlank(message = "Password confirm field cannot be left empty")
    String passwordConfirm;
    @NotBlank(message = "Gender field cannot be left empty")
    String gender;
    @NotBlank(message = "Email field cannot be left empty")
            @Email(message = "Invalid email format", regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
    String email;
    @NotBlank(message = "Phone number field cannot be left empty")
            @Pattern(regexp = "^(0|\\+84)(3[2-9]|5[2689]|7[0-9]|8[1-9]|9[0-9])[0-9]{7}$", message = "Invalid phone number format")
    String phone;
}
