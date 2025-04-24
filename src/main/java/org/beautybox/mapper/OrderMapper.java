package org.beautybox.mapper;

import org.beautybox.entity.OrderItem;
import org.beautybox.entity.OrderProduct;
import org.beautybox.request.OrderRequest;
import org.beautybox.response.OrderResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class OrderMapper {
    @Mappings( {
            @Mapping(source = "paymentType", target = "paymentType", defaultValue = "1"),
            @Mapping(target = "status", constant = "1"),
            @Mapping(target = "orderItems", ignore = true)
        }
    )
    public abstract OrderProduct toOrder(OrderRequest request);



    @Mappings({
        @Mapping(target = "paymentType", expression = "java(this.convertPaymentType(order.getPaymentType()))"),
            @Mapping(target = "userId", source = "order.user.id"),
            @Mapping(target = "totalAmount", expression = "java(this.getTotalAmount(order.getOrderItems()))"),
            @Mapping(target = "orderTime", source = "order.createdAt"),
            @Mapping(target = "status", expression = "java(this.convertStatus(order.getStatus()))"),
            @Mapping(target = "orderItemsResponse", expression = "java(this.convertOrderItemsResponse(order.getOrderItems()))")
    })
    public abstract OrderResponse toResponse(OrderProduct order);

    public abstract OrderResponse.innerResponse toInnerResponse(OrderItem item);

    protected List<OrderResponse.innerResponse> convertOrderItemsResponse(List<OrderItem> orderItems) {
        return orderItems.stream().map(this::toInnerResponse).toList();
    }

    protected String convertStatus(int status){
        return switch (status){
            case 1 -> "Chờ xác nhận";
            case 2 -> "Đang chuẩn bị giao hàng";
            case 3 -> "Đang giao hàng tới bạn";
            case 4 -> "Đã nhận";
            case 5 -> "Đã huỷ đơn";
            case 6 -> "Không nhận hàng";
            case 7 -> "Chờ thanh toán";
            default -> "Không xác định";
        };
    }
    protected long getTotalAmount(List<OrderItem> orderItems){
        long totalAmount = 0;
        for(OrderItem orderItem : orderItems){
            totalAmount = totalAmount + orderItem.getQuantity() * (orderItem.getPrice() - orderItem.getDiscount() * orderItem.getPrice() / 100);
        }
        return totalAmount;
    }
    protected String convertPaymentType(int paymentType) {
        return switch (paymentType) {
            case 1 -> "Thanh toán tiền mặt";
            case 2 -> "Thanh toán qua VNPay";
            default -> "Không xác định";
        };
    }
}
