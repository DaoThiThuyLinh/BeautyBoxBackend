package org.beautybox.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.beautybox.binder.TotalSoldBinder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.automaticindexing.ReindexOnUpdate;
import org.hibernate.search.mapper.pojo.bridge.mapping.annotation.PropertyBinderRef;
import org.hibernate.search.mapper.pojo.extractor.mapping.annotation.ContainerExtract;
import org.hibernate.search.mapper.pojo.extractor.mapping.annotation.ContainerExtraction;
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
            @Column(length = 36)
    String id;
    @Column(nullable = false)
            @FullTextField(analyzer = "vietnameseAnalyzer")
            @KeywordField(name = "name_sort", normalizer = "lowercase", sortable = Sortable.YES)
    String name;
    @Column(columnDefinition = "text")
            @FullTextField(analyzer = "vietnameseAnalyzer")
    String description;
    @GenericField
    Boolean isEnabled;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
            @JoinColumn(name = "category_id")
            @IndexedEmbedded(includePaths = {"id"})
            @OnDelete(action = OnDeleteAction.SET_NULL)
    Category category;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
            @JoinColumn(name = "brand_id")
            @IndexedEmbedded(includePaths = {"id"})
            @OnDelete(action = OnDeleteAction.SET_NULL)
    Brand brand;

    @IndexedEmbedded
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<ProductDetail> productDetails;

    @Transient
    @PropertyBinding(binder = @PropertyBinderRef(type = TotalSoldBinder.class))
    @IndexingDependency(reindexOnUpdate = ReindexOnUpdate.NO, extraction = @ContainerExtraction(extract = ContainerExtract.NO))
    public String getTotalSold() {
        return this.id;
    }
//
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
