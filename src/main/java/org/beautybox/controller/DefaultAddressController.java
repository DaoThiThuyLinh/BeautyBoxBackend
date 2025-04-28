package org.beautybox.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.beautybox.anotation.CurrentUser;
import org.beautybox.entity.User;
import org.beautybox.exception.BeautyBoxException;
import org.beautybox.request.CreateDefaultAddressRequest;
import org.beautybox.request.UpdateDefaultAddressRequest;
import org.beautybox.response.ApiResponse;
import org.beautybox.service.DefaultAddressService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class DefaultAddressController {

    final DefaultAddressService defaultAddressService;

    @PostMapping("/address")
    public ApiResponse add(@RequestBody @Valid CreateDefaultAddressRequest request, @CurrentUser User user) {
        defaultAddressService.add(request, user);
        return ApiResponse.success("Đã thêm địa chỉ mới");
    }

    @PutMapping("/address/change-default/{addressId}")
    public ApiResponse changeDefault(@PathVariable String addressId, @CurrentUser User user) throws BeautyBoxException {
        defaultAddressService.changeDefault(addressId, user);
        return ApiResponse.success("Sửa thành công");
    }

    @DeleteMapping("/address/{id}")
    public ApiResponse delete(@PathVariable String id, @CurrentUser User user) throws BeautyBoxException {
        defaultAddressService.delete(id, user);
        return ApiResponse.success("Xoá thành công");
    }

    @GetMapping("/address")
    public ApiResponse get(@CurrentUser User user) {
        return ApiResponse.success("Danh sách địa chỉ của bạn", defaultAddressService.getAllByUser(user));
    }
}
