package org.beautybox.service;

import org.beautybox.entity.User;
import org.beautybox.request.OrderRequest;
import org.springframework.stereotype.Service;

@Service
public interface OrderService {
    void add(User user, OrderRequest orderRequest);
}
