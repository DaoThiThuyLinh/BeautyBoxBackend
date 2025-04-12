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
    @Column(unique = true, nullable = false, updatable = false)
    long totalAmount;
    @Column(unique = true, nullable = false, updatable = false)
    String notes;
    int paymentType;
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
    @JoinColumn(name = "product_id")
    Product product;
    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
}
