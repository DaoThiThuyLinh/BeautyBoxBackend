package org.beautybox.service;

import org.beautybox.entity.User;
import org.beautybox.request.CreateDefaultAddressRequest;
import org.springframework.stereotype.Service;

@Service
public interface DefaultAddressService {
    void add(CreateDefaultAddressRequest request, User user);
}
