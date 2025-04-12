package org.beautybox.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDetail extends BaseEntity {
    @Id
        @GeneratedValue(strategy = GenerationType.UUID)
            @Column(length = 36)
    String id;
    @Column(columnDefinition = "varchar(100)", unique = true, nullable = false)
            @FullTextField(analyzer = "vietnameseAnalyzer")
    String name;
    @Min(value = 0, message = "Price must be greater than 0")
            @GenericField(sortable = Sortable.YES)
    long price;
    @Min(value = 0, message = "Discount must be greater than 0")
    int discount;
    @Min(value = 0, message = "Stock must be greater than 0")
    int stock;
    @Column(columnDefinition = "text")
            @FullTextField(analyzer = "vietnameseAnalyzer")
    String description;
    @Column(unique = true)
    String imageUrl;
    Boolean status;
    @ManyToOne
            @JoinColumn(name = "product_id")
    Product product;
}
