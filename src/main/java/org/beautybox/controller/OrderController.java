package org.beautybox.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.beautybox.anotation.CurrentUser;
import org.beautybox.entity.User;
import org.beautybox.exception.BeautyBoxException;
import org.beautybox.request.OrderRequest;
import org.beautybox.request.UpdateOrderRequest;
import org.beautybox.response.ApiResponse;
import org.beautybox.service.OrderService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class OrderController {

    final OrderService orderService;

    @PostMapping("/order")
    public ApiResponse order(@RequestBody @Valid OrderRequest orderRequest, HttpServletRequest request, @CurrentUser User user) {
        String result = orderService.add(user, orderRequest, request);
        if(result == null) {
            return ApiResponse.success("Đặt hàng thành công");
        }
        return ApiResponse.success("Chờ thanh toán", result);
    }

    @PutMapping("/admin-api/order")
    public ApiResponse update(@RequestBody @Valid UpdateOrderRequest updateRequest) throws BeautyBoxException {
        orderService.update(updateRequest);
        return ApiResponse.success("Sửa thông tin đơn hàng thành công");
    }

    @GetMapping("/order/{userId}")
    public ApiResponse getOrder(@PathVariable String userId) {
        return ApiResponse.success("Thành công", orderService.get(userId));
    }

    @DeleteMapping("/order")
    public ApiResponse cancelOrder(@RequestParam String orderId, @CurrentUser User user) throws BeautyBoxException {
        orderService.cancelOrder(orderId, user);
        return ApiResponse.success("Thành công");
    }

    @PostMapping("/order/pay-again")
    public ApiResponse payAgain(@RequestParam String orderId, HttpServletRequest request, @CurrentUser User user) throws BeautyBoxException {
        String payUrl = orderService.payAgain(orderId, request, user);
        return ApiResponse.success("Chờ thanh toán", payUrl);
    }
}
