package org.beautybox.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.beautybox.anotation.CurrentUser;
import org.beautybox.entity.User;
import org.beautybox.request.OrderRequest;
import org.beautybox.response.ApiResponse;
import org.beautybox.service.OrderService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {

    final OrderService orderService;

    @PostMapping("/order")
    public ApiResponse order(@RequestBody @Valid OrderRequest orderRequest, @CurrentUser User user) {
        orderService.add(user, orderRequest);
        return ApiResponse.success("Đặt hàng thành công");
    }
}
