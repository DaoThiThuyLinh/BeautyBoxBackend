package org.beautybox.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.beautybox.BeautyBoxApplication;
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
    final UserRepository userRepository;
    final ProductDetailRepository productDetailRepository;

    @SneakyThrows
    @Override
    public void add(User user, OrderRequest orderRequest) {
        ProductDetail productDetail = productDetailRepository.findById(orderRequest.getProductDetailId()).orElseThrow(
                () -> new BeautyBoxException(ErrorDetail.ERR_PRODUCT_NOT_EXISTED)
        );
        long price = productDetail.getPrice();
        int discount = productDetail.getDiscount();
        long totalAmount = (price - price * discount / 100) * orderRequest.getQuantity();
        OrderProduct orderProduct = orderMapper.toOrder(orderRequest);
        orderProduct.setTotalAmount(totalAmount);
        orderProduct.setUser(user);
        orderProduct.setProductDetail(productDetail);

        orderRepository.save(orderProduct);
    }
}
