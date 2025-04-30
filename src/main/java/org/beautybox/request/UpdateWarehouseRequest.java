package org.beautybox.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateWarehouseRequest{
    @NotBlank(message = "Lựa chọn kho hàng bạn cần sửa")
    String id;
    @NotNull(message = "Đang thiếu ngày nhập kho")
    LocalDate entryDate;
    @NotNull(message = "Đang thiếu giá nhập")
    Long entryPrice;
    String entryPhoneNumber;
    String entryPlace;
    @NotNull(message = "Không được bỏ trống số lượng nhập")
    Integer entryQuantity;
}
