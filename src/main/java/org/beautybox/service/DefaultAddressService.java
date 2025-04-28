package org.beautybox.service;

import org.beautybox.entity.User;
import org.beautybox.exception.BeautyBoxException;
import org.beautybox.request.CreateDefaultAddressRequest;
import org.beautybox.request.UpdateDefaultAddressRequest;
import org.springframework.stereotype.Service;

@Service
public interface DefaultAddressService {
    void add(CreateDefaultAddressRequest request, User user);
    void update(UpdateDefaultAddressRequest updateRequest, User user) throws BeautyBoxException;
    void delete(String id, User user) throws BeautyBoxException;

}
