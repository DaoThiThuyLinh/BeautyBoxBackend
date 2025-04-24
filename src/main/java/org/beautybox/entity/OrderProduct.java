package org.beautybox.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

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
    @Column(updatable = false)
    String notes;
    /*
    1. Thanh toán tiền mặt
    2. Thanh toán qua VNPay
     */
    @Column(nullable = false, updatable = false)
    int paymentType;
    /*
    1. Chờ xác nhận
    2. Đang chuẩn bị giao hàng
    3. Đang giao hàng tới
    4. Đã nhận
    5. Đã huỷ đơn
    6. Không nhận hàng
    7. Chờ thanh toán
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

    @ManyToOne
    @JoinColumn
    User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<OrderItem> orderItems;
}
