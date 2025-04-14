package org.beautybox.mapper;

import org.beautybox.entity.OrderProduct;
import org.beautybox.request.OrderRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public abstract class OrderMapper {
    @Mappings( {
            @Mapping(source = "paymentType", target = "paymentType", defaultValue = "1"),
            @Mapping(target = "status", constant = "1")
        }
    )
    public abstract OrderProduct toOrder(OrderRequest request);
}
