package org.beautybox.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class OrderItem {
    @Id
            @GeneratedValue(strategy = GenerationType.UUID)
    String id;
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
    @Min(value = 1, message = "Quantity must be greater than 1")
    @Column(nullable = false, updatable = false)
    int quantity;
    @Column(updatable = false)
    String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
            @JoinColumn
    OrderProduct order;
}
