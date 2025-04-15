package org.beautybox.service.impl;

import lombok.RequiredArgsConstructor;
import org.beautybox.entity.Cart;
import org.beautybox.entity.ProductDetail;
import org.beautybox.entity.User;
import org.beautybox.exception.BeautyBoxException;
import org.beautybox.exception.ErrorDetail;
import org.beautybox.repository.CartRepository;
import org.beautybox.repository.ProductDetailRepository;
import org.beautybox.request.CreateCartRequest;
import org.beautybox.service.CartService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    final ProductDetailRepository productDetailRepository;
    final CartRepository cartRepository;

    @Override
    public void add(User user, CreateCartRequest request) throws BeautyBoxException{
        if(cartRepository.existsByUserAndProductDetail(user.getId(), request.getProductDetailId())){
            throw new BeautyBoxException(ErrorDetail.ERR_CART_EXISTED);
        }
        ProductDetail productDetail = productDetailRepository.findById(request.getProductDetailId()).orElseThrow(
                () -> new BeautyBoxException(ErrorDetail.ERR_PRODUCT_NOT_EXISTED)
        );
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setProductDetail(productDetail);
        cart.setQuantity(request.getQuantity());
        cartRepository.save(cart);
    }
}
