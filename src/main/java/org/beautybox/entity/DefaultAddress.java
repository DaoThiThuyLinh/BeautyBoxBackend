package org.beautybox.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DefaultAddress extends BaseEntity{
    @Id
            @GeneratedValue(strategy = GenerationType.UUID)
    String id;
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
}
