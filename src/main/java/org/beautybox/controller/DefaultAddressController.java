package org.beautybox.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.beautybox.anotation.CurrentUser;
import org.beautybox.entity.User;
import org.beautybox.request.CreateDefaultAddressRequest;
import org.beautybox.response.ApiResponse;
import org.beautybox.service.DefaultAddressService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DefaultAddressController {

    final DefaultAddressService defaultAddressService;

    @PostMapping("/address")
    public ApiResponse add(@RequestBody @Valid CreateDefaultAddressRequest request, @CurrentUser User user) {
        defaultAddressService.add(request, user);
        return ApiResponse.success("Địa chỉ mặc định đã được thêm");
    }
}
