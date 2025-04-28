package org.beautybox.service.impl;

import lombok.RequiredArgsConstructor;
import org.beautybox.entity.DefaultAddress;
import org.beautybox.entity.User;
import org.beautybox.mapper.DefaultAddressMapper;
import org.beautybox.repository.DefaultAddressRepository;
import org.beautybox.request.CreateDefaultAddressRequest;
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
}
