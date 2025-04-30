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
import org.beautybox.response.DefaultAddressResponse;
import org.beautybox.service.DefaultAddressService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DefaultAddressServiceImpl implements DefaultAddressService {

    final DefaultAddressMapper defaultAddressMapper;
    final DefaultAddressRepository defaultAddressRepository;

    @Override
    public void add(CreateDefaultAddressRequest request, User user) {
        DefaultAddress defaultAddress = defaultAddressMapper.toDefaultAddress(request);
        defaultAddress.setUser(user);
        defaultAddress.setIsDefault(request.getIsDefault());
        if(defaultAddress.getIsDefault()){
            List<DefaultAddress> defaultAddresses = defaultAddressRepository.getByUser(user.getId());
            for(DefaultAddress item : defaultAddresses) {
                item.setIsDefault(false);
                defaultAddressRepository.save(item);
            }
        }
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
    public void changeDefault(String id, User user) throws BeautyBoxException {
        DefaultAddress defaultAddress = defaultAddressRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Địa chỉ không tồn tại")
        );
        List<DefaultAddress> defaultAddresses = defaultAddressRepository.getByUser(user.getId());
        for(DefaultAddress item : defaultAddresses) {
            item.setIsDefault(false);
            defaultAddressRepository.save(item);
        }
        defaultAddress.setIsDefault(true);
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
        List<DefaultAddress> defaultAddresses = defaultAddressRepository.getByUser(user.getId());
        for(DefaultAddress item : defaultAddresses) {
            item.setIsDefault(true);
            defaultAddressRepository.save(item);
            break;
        }
        defaultAddressRepository.delete(defaultAddress);
    }

    @Override
    public List<DefaultAddressResponse> getAllByUser(User user) {
        return defaultAddressRepository.getByUser(user.getId()).stream().map(defaultAddressMapper::toResponse).toList();
    }
}
