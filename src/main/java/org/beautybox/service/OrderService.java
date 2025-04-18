package org.beautybox.service;

import jakarta.servlet.http.HttpServletRequest;
import org.beautybox.entity.User;
import org.beautybox.exception.BeautyBoxException;
import org.beautybox.request.OrderRequest;
import org.beautybox.response.OrderResponse;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

@Service
public interface OrderService {
    String add(User user, OrderRequest orderRequest, HttpServletRequest request);
    String executePaymentResult(Map<String, String> params, HttpServletRequest request) throws BeautyBoxException, UnsupportedEncodingException;
    List<OrderResponse> get(String userId);
}
