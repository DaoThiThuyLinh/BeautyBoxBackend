package org.beautybox.service;

import org.beautybox.entity.User;
import org.beautybox.exception.BeautyBoxException;
import org.beautybox.request.CreateDefaultAddressRequest;
import org.beautybox.request.UpdateDefaultAddressRequest;
import org.beautybox.response.DefaultAddressResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DefaultAddressService {
    void add(CreateDefaultAddressRequest request, User user);
    void update(UpdateDefaultAddressRequest updateRequest, User user) throws BeautyBoxException;
    void changeDefault(String id, User user) throws BeautyBoxException;
    void delete(String id, User user) throws BeautyBoxException;
    List<DefaultAddressResponse> getAllByUser(User user);
}
