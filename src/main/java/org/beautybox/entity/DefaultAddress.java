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
    @Column(nullable = false, length = 50)
    String name;
    @Column(nullable = false, length = 50)
    String commune;
    @Column(nullable = false, length = 50)
    String district;
    @Column(nullable = false, length = 50)
    String province;
    @Column(nullable = false, length = 100)
    String detailAddress;
    @Column(nullable = false, length = 50)
    String recipientName;
    @Column(nullable = false, length = 12)
    String recipientPhoneNumber;
    boolean isDefault;

    @ManyToOne(fetch = FetchType.LAZY)
            @JoinColumn(nullable = false, updatable = false)
    User user;
}
