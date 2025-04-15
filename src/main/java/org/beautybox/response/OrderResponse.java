package org.beautybox.response;


import java.time.LocalDateTime;

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
    String productName;
    String productDetailName;
    LocalDateTime orderTime;
}
