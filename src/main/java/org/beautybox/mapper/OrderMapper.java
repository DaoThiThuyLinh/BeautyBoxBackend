package org.beautybox.mapper;

import org.beautybox.entity.OrderProduct;
import org.beautybox.request.OrderRequest;
import org.beautybox.response.OrderResponse;
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



    @Mappings({
        @Mapping(target = "paymentType", expression = "java(this.convertPaymentType(order.getPaymentType()))"),
            @Mapping(target = "userId", source = "order.user.id"),
            @Mapping(target = "totalAmount", expression = "java(this.getTotalAmount(order.getQuantity(), order.getPrice(), order.getDiscount()))"),
            @Mapping(target = "orderTime", source = "order.createdAt")
    })
    public abstract OrderResponse toResponse(OrderProduct order);

    protected long getTotalAmount(int quantity, long price, int discount){
        return quantity * (price - price * discount / 100);
    }
    protected String convertPaymentType(int paymentType) {
        return switch (paymentType) {
            case 1 -> "Thanh toán tiền mặt";
            case 2 -> "Thanh toán qua VNPay";
            default -> "Không xác định";
        };
    }
}
