package org.beautybox.service;

import org.beautybox.request.OrderRequest;
import org.springframework.stereotype.Service;

@Service
public interface OrderService {
    void add(OrderRequest orderRequest);
}
