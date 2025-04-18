package org.beautybox.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {
    String id;
    int quantity;
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
    String productId;
    String productName;
    String description;
    String productDetailId;
    String productDetailName;
    long price;
    int discount;
    String imageUrl;
    String userId;
    LocalDateTime orderTime;
}
