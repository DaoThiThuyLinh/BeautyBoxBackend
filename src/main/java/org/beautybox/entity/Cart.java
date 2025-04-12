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
public class Cart extends BaseEntity {
    @Id
        @GeneratedValue(strategy = GenerationType.UUID)
            @Column(length = 36)
    String id;
    @Column(columnDefinition = "int check (quantity > 0)")
    int quantity;
    @ManyToOne
            @JoinColumn(name = "product_id")
    Product product;
    @ManyToOne
            @JoinColumn(name = "user_id")
    User user;
}
