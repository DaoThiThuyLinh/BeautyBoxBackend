package org.beautybox.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.beautybox.entity.OrderProduct;
import org.beautybox.entity.ProductDetail;
import org.beautybox.entity.User;
import org.beautybox.exception.BeautyBoxException;
import org.beautybox.exception.ErrorDetail;
import org.beautybox.mapper.OrderMapper;
import org.beautybox.repository.OrderRepository;
import org.beautybox.repository.ProductDetailRepository;
import org.beautybox.repository.UserRepository;
import org.beautybox.request.OrderRequest;
import org.beautybox.service.OrderService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    final OrderMapper orderMapper;
    final OrderRepository orderRepository;
    final ProductDetailRepository productDetailRepository;

    @SneakyThrows
    @Override
    public void add(User user, OrderRequest orderRequest) {
        ProductDetail productDetail = productDetailRepository.findById(orderRequest.getProductDetailId()).orElseThrow(
                () -> new BeautyBoxException(ErrorDetail.ERR_PRODUCT_NOT_EXISTED)
        );

        OrderProduct orderProduct = orderMapper.toOrder(orderRequest);

        orderProduct.setUser(user);
        orderProduct.setProductId(productDetail.getProduct().getId());
        orderProduct.setProductName(productDetail.getProduct().getName());
        orderProduct.setDescription(productDetail.getProduct().getDescription());
        orderProduct.setProductDetailId(productDetail.getId());
        orderProduct.setProductDetailName(productDetail.getName());
        orderProduct.setPrice(productDetail.getPrice());
        orderProduct.setDiscount(productDetail.getDiscount());
        orderProduct.setImageUrl(productDetail.getImageUrl());
        orderRepository.save(orderProduct);
    }
}
