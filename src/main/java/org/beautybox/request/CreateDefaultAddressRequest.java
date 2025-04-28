package org.beautybox.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateDefaultAddressRequest {
    @NotBlank(message = "Không được bỏ trống tên của địa chỉ này")
    String name;
    @NotBlank(message = "Không được bỏ trống xã")
    String commune;
    @NotBlank(message = "Không được bỏ trống huyện")
    String district;
    @NotBlank(message = "Không được bỏ trống tỉnh")
    String province;
    String detailAddress;
    @NotBlank(message = "Không được bỏ trống tên người nhận hàng")
    String recipientName;
    @NotBlank(message = "Không được bỏ trống số điện thoại người nhận hàng")
            @Pattern(regexp = "^(0|\\+84)(3[2-9]|5[2689]|7[0-9]|8[1-9]|9[0-9])[0-9]{7}$", message = "Số điện thoại không đúng định dạng")
    String recipientPhoneNumber;
}
