package org.beautybox.service;

import org.beautybox.entity.User;
import org.beautybox.exception.BeautyBoxException;
import org.beautybox.request.CreateCartRequest;
import org.beautybox.request.UpdateCartRequest;
import org.beautybox.response.CartResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CartService {
    void add(User user, CreateCartRequest request) throws BeautyBoxException;
    void delete(String cartId, User user) throws BeautyBoxException;
    void updateCart(UpdateCartRequest updateRequest, User user) throws BeautyBoxException;
    List<CartResponse> getCart(User user) ;

}
