package org.beautybox.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {
    String id;
    long totalAmount;
    String notes;
    String paymentType;
    String status;
    String orderCode;
    String commune;
    String district;
    String province;
    String detailAddress;
    String recipientName;
    String recipientPhoneNumber;
    String userId;
    LocalDateTime orderTime;
    List<innerResponse> orderItemsResponse;


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class innerResponse{
        String id;
        int quantity;
        String productId;
        String productName;
        String description;
        String productDetailId;
        String productDetailName;
        long price;
        long newPrice;
        int discount;
        String imageUrl;
    }
}
