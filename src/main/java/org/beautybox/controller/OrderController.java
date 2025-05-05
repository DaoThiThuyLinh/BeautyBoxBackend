package org.beautybox.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

import java.time.LocalDate;


@RestController
@RequiredArgsConstructor
public class OrderController {

    final OrderService orderService;

    @PostMapping("/order")
    public ApiResponse order(@RequestBody @Valid OrderRequest orderRequest, HttpServletRequest request, @CurrentUser User user) throws BeautyBoxException {
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

    @Operation(summary = "Lấy ra danh sách order", parameters = {
            @Parameter(name = "s", description = "ProductId hoặc OrderId")
    })
    @GetMapping("/order")
    public ApiResponse getOrder(@RequestParam(required = false, defaultValue = "") String s,
                                @RequestParam(required = false, defaultValue = "") String userId,
                                @RequestParam(required = false, defaultValue = "1") int pageIndex,
                                @RequestParam(required = false, defaultValue = "40") int pageSize,
                                @RequestParam(required = false, defaultValue = "0") int status,
                                @RequestParam(required = false) LocalDate fromDate,
                                @RequestParam(required = false) LocalDate toDate) {
        if(fromDate == null) fromDate = LocalDate.of(1900, 1, 1);
        if(toDate == null) toDate = LocalDate.of(2550, 1, 1);
        return ApiResponse.success("Thành công", orderService.get(s, userId, pageIndex, pageSize, status, fromDate, toDate));
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
