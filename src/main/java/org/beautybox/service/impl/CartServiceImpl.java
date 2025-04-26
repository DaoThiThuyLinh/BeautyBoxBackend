package org.beautybox.service.impl;

import lombok.RequiredArgsConstructor;
import org.beautybox.entity.Cart;
import org.beautybox.entity.ProductDetail;
import org.beautybox.entity.User;
import org.beautybox.exception.BeautyBoxException;
import org.beautybox.exception.ErrorDetail;
import org.beautybox.mapper.ProductMapper;
import org.beautybox.repository.CartRepository;
import org.beautybox.repository.OrderItemRepository;
import org.beautybox.repository.ProductDetailRepository;
import org.beautybox.request.CreateCartRequest;
import org.beautybox.request.UpdateCartRequest;
import org.beautybox.response.CartResponse;
import org.beautybox.service.CartService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    final ProductDetailRepository productDetailRepository;
    final OrderItemRepository orderItemRepository;
    final ProductMapper productMapper;
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

    @Override
    public void delete(String cartId, User user) throws BeautyBoxException {
        Cart cart = cartRepository.findById(cartId).orElseThrow(
                () -> new BeautyBoxException(ErrorDetail.ERR_CART_NOT_EXISTED)
        );
        if(!cart.getUser().getId().equals(user.getId())){
            throw new BeautyBoxException(ErrorDetail.ERR_ORDER_USER_NOT_CORRECT);
        }
        cartRepository.delete(cart);
    }

    @Override
    public void updateCart(UpdateCartRequest updateRequest, User user) throws BeautyBoxException {
        Cart cart = cartRepository.findById(updateRequest.getId()).orElseThrow(
                () -> new BeautyBoxException(ErrorDetail.ERR_CART_NOT_EXISTED)
        );
        if(!cart.getUser().getId().equals(user.getId())){
            throw new BeautyBoxException(ErrorDetail.ERR_ORDER_USER_NOT_CORRECT);
        }
        cart.setQuantity(updateRequest.getQuantity());
        cartRepository.save(cart);
    }

    @Override
    public List<CartResponse> getCart(User user) {
        return cartRepository.findByUserId(user.getId()).stream().map( item -> {
            CartResponse cartResponse = new CartResponse();
            cartResponse.setId(item.getId());
            cartResponse.setCreatedAt(item.getCreatedAt());
            cartResponse.setQuantity(item.getQuantity());
            cartResponse.setProductDetail(productMapper.toProductDetailResponse(item.getProductDetail()));
            cartResponse.setIsEnabled(true);
            cartResponse.setProductId(item.getProductDetail().getProduct().getId());
            long totalSold = orderItemRepository.sumByProductDetailId(item.getProductDetail().getId());
            if(item.getProductDetail().getStock() - totalSold - item.getQuantity() <= 0){
                cartResponse.setIsEnabled(false);
                cartResponse.setMessageStatus("Số lượng sản phẩm trong kho không đủ");
            }
            if(!item.getProductDetail().getIsEnabled()){
                cartResponse.setIsEnabled(false);
                cartResponse.setMessageStatus("Sản phẩm đã bị xoá");
            }
            return cartResponse;
        }).toList();
    }
}
