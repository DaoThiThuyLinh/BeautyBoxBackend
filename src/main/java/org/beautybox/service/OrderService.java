package org.beautybox.service;

import jakarta.servlet.http.HttpServletRequest;
import org.beautybox.entity.User;
import org.beautybox.exception.BeautyBoxException;
import org.beautybox.request.OrderRequest;
import org.beautybox.request.UpdateOrderRequest;
import org.beautybox.response.OrderResponse;
import org.beautybox.response.PageResponse;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Map;

@Service
public interface OrderService {
    String add(User user, OrderRequest orderRequest, HttpServletRequest request) throws BeautyBoxException;
    void cancelOrder(String orderId, User user) throws BeautyBoxException;
    void update(UpdateOrderRequest updateRequest) throws BeautyBoxException;
    String payAgain(String orderId, HttpServletRequest request, User user) throws BeautyBoxException;
    String executePaymentResult(Map<String, String> params, HttpServletRequest request) throws BeautyBoxException, UnsupportedEncodingException;
    PageResponse<OrderResponse> get(String s, String userId, int pageIndex, int pageSize, int status);
}
