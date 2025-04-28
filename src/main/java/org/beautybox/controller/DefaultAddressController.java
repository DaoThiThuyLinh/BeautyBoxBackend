package org.beautybox.controller;

import com.cloudinary.Api;
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
        return ApiResponse.success("Địa chỉ mặc định đã được thêm");
    }

    @PutMapping("/address")
    public ApiResponse update(@RequestBody @Valid UpdateDefaultAddressRequest request, @CurrentUser User user) throws BeautyBoxException {
        defaultAddressService.update(request, user);
        return ApiResponse.success("Sửa thành công");
    }

    @DeleteMapping("/address/{id}")
    public ApiResponse delete(@PathVariable String id, @CurrentUser User user) throws BeautyBoxException {
        defaultAddressService.delete(id, user);
        return ApiResponse.success("Xoá thành công");
    }
}
