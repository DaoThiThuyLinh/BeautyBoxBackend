package org.beautybox.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Review extends BaseEntity {
    @Id
        @GeneratedValue(strategy = GenerationType.UUID)
            @Column(length = 36)
    String id;
    @Min(value = 0, message = "Rating must be greater than 0")
            @Max(value = 5, message = "Rating must be less than 0")
    int rating;
    @Column(columnDefinition = "text")
    String comment;
    @ManyToOne
            @JoinColumn(name = "user_id")
    User user;
    @ManyToOne
            @JoinColumn(name = "product_id")
    Product product;
}
