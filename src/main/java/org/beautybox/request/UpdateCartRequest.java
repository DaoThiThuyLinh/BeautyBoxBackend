package org.beautybox.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateCartRequest {
    @NotBlank(message = "Lựa chọn giỏ hàng để sửa")
    String id;
    @NotNull(message = "Không được bỏ trống số lượng đặt hàng")
    @Min(value = 1, message = "Số lượng sản phẩm tối thiểu là 1")
    Integer quantity;
}
