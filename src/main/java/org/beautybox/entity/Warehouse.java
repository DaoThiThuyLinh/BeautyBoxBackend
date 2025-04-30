package org.beautybox.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Warehouse extends BaseEntity {
    @Id
        @GeneratedValue(strategy = GenerationType.UUID)
            @Column(length = 36)
    String id;
    @Column(nullable = false)
    LocalDate entryDate;
    @Column(nullable = false)
    Long entryPrice;
    @Column(columnDefinition = "varchar(12)")
    String entryPhoneNumber;
    String entryPlace;
    @Column(nullable = false)
    int entryQuantity;
    @ManyToOne(fetch = FetchType.LAZY)
    ProductDetail productDetail;
}
