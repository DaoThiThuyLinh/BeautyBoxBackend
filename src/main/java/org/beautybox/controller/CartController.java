package org.beautybox.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.beautybox.anotation.CurrentUser;
import org.beautybox.entity.User;
import org.beautybox.exception.BeautyBoxException;
import org.beautybox.request.CreateCartRequest;
import org.beautybox.response.ApiResponse;
import org.beautybox.service.CartService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CartController {

    final CartService cartService;

    @PostMapping("/cart")
    public ApiResponse addToCart(@RequestBody @Valid CreateCartRequest request, @CurrentUser User user) throws BeautyBoxException {
        cartService.add(user, request);
        return ApiResponse.success("Đã thêm sản phẩm vào giỏ hàng");
    }

    @DeleteMapping("/cart/{cartId}")
    public ApiResponse deleteCart(@PathVariable String cartId, @CurrentUser User user) throws BeautyBoxException {
        cartService.delete(cartId, user);
        return ApiResponse.success("Xoá thành công");
    }



}
