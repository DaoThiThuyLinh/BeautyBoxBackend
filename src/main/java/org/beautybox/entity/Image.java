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
public class Image extends BaseEntity {
    @Id
        @GeneratedValue(strategy = GenerationType.UUID)
            @Column(length = 36)
    String id;
    @Column(unique = true, nullable = false)
    String url;
    @ManyToOne
            @JoinColumn(name = "product_id")
    Product product;
}
