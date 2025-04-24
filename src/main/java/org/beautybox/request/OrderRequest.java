package org.beautybox.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderRequest {
    String note;
    @NotNull(message = "Không được bỏ trống phương thức thanh toán")
            @Min(value = 1, message = "Chỉ thanh toán tiền mặt hoặc qua VNPay")
            @Max(value = 2, message = "Chỉ thanh toán tiền mặt hoặc qua VNPay")
    Integer paymentType;
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
    String recipientPhoneNumber;
    List<innerRequest> orderItems;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class innerRequest{
        @NotBlank(message = "Bạn chưa lựa chọn sản phẩm")
        String productDetailId;
        @NotNull(message = "Không được bỏ trống số lượng đặt hàng")
        @Min(value = 1, message = "Số lượng đơn hàng tối thiểu là 1")
        Integer quantity;
    }
}
