package org.beautybox.service;

import jakarta.servlet.http.HttpServletRequest;
import org.beautybox.entity.User;
import org.beautybox.exception.BeautyBoxException;
import org.beautybox.request.OrderRequest;
import org.beautybox.request.UpdateOrderRequest;
import org.beautybox.response.OrderResponse;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

@Service
public interface OrderService {
    String add(User user, OrderRequest orderRequest, HttpServletRequest request);
    void cancelOrder(String orderId, User user) throws BeautyBoxException;
    void update(UpdateOrderRequest updateRequest) throws BeautyBoxException;
    String payAgain(String orderId, HttpServletRequest request, User user) throws BeautyBoxException;
    String executePaymentResult(Map<String, String> params, HttpServletRequest request) throws BeautyBoxException, UnsupportedEncodingException;
    List<OrderResponse> get(String userId);
}
