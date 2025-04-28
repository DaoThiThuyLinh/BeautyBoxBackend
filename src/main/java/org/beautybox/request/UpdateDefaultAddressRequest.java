package org.beautybox.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateDefaultAddressRequest extends CreateDefaultAddressRequest{
    @NotBlank(message = "Bạn cần lựa chọn địa chỉ để sửa")
    String id;
}
