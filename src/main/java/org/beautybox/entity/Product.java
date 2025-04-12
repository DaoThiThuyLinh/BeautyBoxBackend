package org.beautybox.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Indexed
public class Product extends BaseEntity {
    @Id
        @GeneratedValue(strategy = GenerationType.UUID)
            @Column(length = 36)
    String id;
    @Column(nullable = false)
            @FullTextField(analyzer = "vietnameseAnalyzer")
            @KeywordField(name = "name_sort", normalizer = "lowercase", sortable = Sortable.YES)
    String name;
    @Column(columnDefinition = "text")
            @FullTextField(analyzer = "vietnameseAnalyzer")
    String description;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
            @JoinColumn(name = "category_id")
            @IndexedEmbedded(includePaths = {"id"})
    Category category;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
            @JoinColumn(name = "brand_id")
            @IndexedEmbedded(includePaths = {"id"})
    Brand brand;

    @IndexedEmbedded
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<ProductDetail> productDetails;

//    @GenericField(name = "sizeProductDetail", sortable = Sortable.YES)
//    @AssociationInverseSide(inversePath = @ObjectPath({
//            @PropertyValue(propertyName = "product")
//    }))
//    @IndexingDependency(derivedFrom = @ObjectPath({
//            @PropertyValue(propertyName = "productDetails")
//    }))
//    public int getSizeProductDetail(){
//        return productDetails.size();
//    }
}
