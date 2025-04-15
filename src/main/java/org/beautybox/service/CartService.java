package org.beautybox.service;

import org.beautybox.entity.User;
import org.beautybox.exception.BeautyBoxException;
import org.beautybox.request.CreateCartRequest;
import org.springframework.stereotype.Service;

@Service
public interface CartService {
    void add(User user, CreateCartRequest request) throws BeautyBoxException;
}
