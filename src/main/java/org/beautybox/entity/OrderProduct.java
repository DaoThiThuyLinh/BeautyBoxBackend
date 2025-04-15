package org.beautybox.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderProduct extends BaseEntity {
    @Id
        @GeneratedValue(strategy = GenerationType.UUID)
            @Column(length = 36)
    String id;
    @Min(value = 1, message = "Quantity must be greater than 1")
    @Column(nullable = false, updatable = false)
    int quantity;
    @Column(unique = true, updatable = false)
    String notes;
    /*
    1. Thanh toán tiền mặt
    2. Thanh toán qua VNPay
     */
    int paymentType;
    /*
    1. Chờ xác nhận
    2. Đang chuẩn bị giao hàng
    3. Đang giao hàng tới
    4. Đã nhận
    5. Đã huỷ đơn
    6. Không nhận hàng
     */
    int status;
    @Column(unique = true)
    String orderCode;
    @Column(updatable = false, nullable = false, length = 50)
    String commune;
    @Column(updatable = false, nullable = false, length = 50)
    String district;
    @Column(updatable = false, nullable = false, length = 50)
    String province;
    @Column(updatable = false, nullable = false, length = 100)
    String detailAddress;
    @Column(updatable = false, nullable = false, length = 50)
    String recipientName;
    @Column(updatable = false, nullable = false, length = 12)
    String recipientPhoneNumber;
    @Column(length = 36, nullable = false, updatable = false)
    String productId;
    @Column(columnDefinition = "varchar(100)", nullable = false, updatable = false)
    String productName;
    @Column(columnDefinition = "text", updatable = false)
    String description;
    @Column(length = 36, nullable = false, updatable = false)
    String productDetailId;
    @Column(columnDefinition = "varchar(100)", nullable = false, updatable = false)
    String productDetailName;
    @Column(nullable = false, updatable = false)
    long price;
    @Column(nullable = false, updatable = false)
    int discount;
    @Column(nullable = false, updatable = false)
    String imageUrl;

    @ManyToOne
    @JoinColumn
    User user;
}
