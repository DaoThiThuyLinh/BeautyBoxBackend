package org.beautybox.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateCartRequest {
    @NotNull(message = "Không được bỏ trống số lượng đặt hàng")
    @Min(value = 1, message = "Số lượng đơn hàng tối thiểu là 1")
    Integer quantity;
    @NotBlank(message = "Không được bỏ trống sản phẩm")
    String productDetailId;
}
