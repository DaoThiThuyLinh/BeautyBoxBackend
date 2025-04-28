package org.beautybox.service.impl;

import lombok.RequiredArgsConstructor;
import org.beautybox.entity.DefaultAddress;
import org.beautybox.entity.User;
import org.beautybox.exception.BeautyBoxException;
import org.beautybox.exception.ErrorDetail;
import org.beautybox.mapper.DefaultAddressMapper;
import org.beautybox.repository.DefaultAddressRepository;
import org.beautybox.request.CreateDefaultAddressRequest;
import org.beautybox.request.UpdateDefaultAddressRequest;
import org.beautybox.service.DefaultAddressService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultAddressServiceImpl implements DefaultAddressService {

    final DefaultAddressMapper defaultAddressMapper;
    final DefaultAddressRepository defaultAddressRepository;

    @Override
    public void add(CreateDefaultAddressRequest request, User user) {
        DefaultAddress defaultAddress = defaultAddressMapper.toDefaultAddress(request);
        defaultAddress.setUser(user);
        defaultAddressRepository.save(defaultAddress);
    }

    @Override
    public void update(UpdateDefaultAddressRequest updateRequest, User user) throws BeautyBoxException {
        DefaultAddress defaultAddress = defaultAddressRepository.findById(updateRequest.getId()).orElseThrow(
                () -> new RuntimeException("Địa chỉ không tồn tại")
        );
        if(!defaultAddress.getUser().getId().equals(user.getId())) {
            throw new BeautyBoxException(ErrorDetail.ERR_ORDER_USER_NOT_CORRECT);
        }
        defaultAddressMapper.update(defaultAddress, updateRequest);
        defaultAddressRepository.save(defaultAddress);
    }

    @Override
    public void delete(String id, User user) throws BeautyBoxException {
        DefaultAddress defaultAddress = defaultAddressRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Địa chỉ không tồn tại")
        );
        if(!defaultAddress.getUser().getId().equals(user.getId())) {
            throw new BeautyBoxException(ErrorDetail.ERR_ORDER_USER_NOT_CORRECT);
        }
        defaultAddressRepository.delete(defaultAddress);
    }
}
