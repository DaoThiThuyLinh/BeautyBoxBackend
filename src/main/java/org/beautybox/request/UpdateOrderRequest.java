package org.beautybox.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateOrderRequest {
    @NotBlank(message = "Chưa lựa chọn đối tượng sửa")
    String orderId;
    String orderCode;
    @Min(value = 1, message = "Trạng thái đơn hàng không hợp lệ")
    @Max(value = 7, message = "Trạng thái đơn hàng không hợp lệ")
    int status;
}
