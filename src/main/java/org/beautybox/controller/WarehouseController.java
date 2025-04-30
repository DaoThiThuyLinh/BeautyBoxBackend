package org.beautybox.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.beautybox.exception.BeautyBoxException;
import org.beautybox.request.CreateWarehouseRequest;
import org.beautybox.request.UpdateWarehouseRequest;
import org.beautybox.response.ApiResponse;
import org.beautybox.service.WarehouseService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class WarehouseController {

    final WarehouseService warehouseService;

    @PostMapping("/admin-api/warehouse")
    public ApiResponse create(@RequestBody @Valid CreateWarehouseRequest request) throws BeautyBoxException {
        warehouseService.add(request);
        return  ApiResponse.success("Thêm kho hàng thành công");
    }

    @GetMapping("/admin-api/warehouse")
    public ApiResponse getAll(@RequestParam String productDetailId,
                              @RequestParam(required = false, defaultValue = "1") int pageIndex,
                              @RequestParam(required = false, defaultValue = "40") int pageSize) throws BeautyBoxException {
        return ApiResponse.success("Danh sách kho hàng của sản phẩm", warehouseService.getAllByProductDetail(productDetailId, pageIndex, pageSize));
    }

    @PutMapping("/admin-api/warehouse")
    public ApiResponse update(@RequestBody @Valid UpdateWarehouseRequest request) throws BeautyBoxException {
        warehouseService.update(request);
        return ApiResponse.success("Sửa thành công");
    }

    @DeleteMapping("/admin-api/warehouse/{productDetailId}")
    public ApiResponse delete(@PathVariable String productDetailId) throws BeautyBoxException {
        warehouseService.delete(productDetailId);
        return ApiResponse.success("Xoá thành công");
    }
}
