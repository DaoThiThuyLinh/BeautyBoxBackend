package org.beautybox.service;

import org.beautybox.exception.BeautyBoxException;
import org.beautybox.request.CreateWarehouseRequest;
import org.beautybox.request.UpdateWarehouseRequest;
import org.beautybox.response.PageResponse;
import org.beautybox.response.WarehouseResponse;
import org.springframework.stereotype.Service;

@Service
public interface WarehouseService {
    void add(CreateWarehouseRequest createRequest) throws BeautyBoxException;
    void update(UpdateWarehouseRequest updateRequest) throws BeautyBoxException;
    void delete(String warehouseId) throws BeautyBoxException;
    PageResponse<WarehouseResponse> getAllByProductDetail(String productDetailId, int pageIndex, int pageSize)  throws BeautyBoxException;
}
