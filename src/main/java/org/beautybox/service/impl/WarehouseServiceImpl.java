package org.beautybox.service.impl;

import lombok.RequiredArgsConstructor;
import org.beautybox.entity.ProductDetail;
import org.beautybox.entity.Warehouse;
import org.beautybox.exception.BeautyBoxException;
import org.beautybox.exception.ErrorDetail;
import org.beautybox.mapper.WarehouseMapper;
import org.beautybox.repository.ProductDetailRepository;
import org.beautybox.repository.WarehouseRepository;
import org.beautybox.request.CreateWarehouseRequest;
import org.beautybox.request.UpdateWarehouseRequest;
import org.beautybox.response.PageResponse;
import org.beautybox.response.WarehouseResponse;
import org.beautybox.service.WarehouseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {

    final WarehouseMapper warehouseMapper;
    final ProductDetailRepository productDetailRepository;
    final WarehouseRepository warehouseRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(CreateWarehouseRequest createRequest) throws BeautyBoxException{
        ProductDetail productDetail = productDetailRepository.findById(createRequest.getProductDetailId()).orElseThrow(
                () -> new BeautyBoxException(ErrorDetail.ERR_PRODUCT_NOT_EXISTED)
        );
        Warehouse warehouse = warehouseMapper.toWarehouse(createRequest);
        warehouse.setProductDetail(productDetail);
        warehouseRepository.save(warehouse);
        updateStock(productDetail);
    }

    private void updateStock(ProductDetail productDetail){
        List<Warehouse> warehousesOfProductDetail = warehouseRepository.getAllByProductId(productDetail.getId(), Pageable.unpaged()).getContent();
        int stock = 0;
        for(Warehouse item : warehousesOfProductDetail){
            stock = stock + item.getEntryQuantity();
        }
        productDetail.setStock(stock);
        productDetailRepository.save(productDetail);
    }

    @Override
    public PageResponse<WarehouseResponse> getAllByProductDetail(String productDetailId, int pageIndex, int pageSize) throws BeautyBoxException {
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize, Sort.by(Sort.Direction.DESC, "entryDate"));
        Page<Warehouse> warehousesPage = warehouseRepository.getAllByProductId(productDetailId, pageable);
        List<WarehouseResponse> contents = warehousesPage.stream().map(warehouseMapper::toResponse).toList();
        return PageResponse.<WarehouseResponse>
                builder()
                .pageIndex(pageIndex)
                .pageSize(pageSize)
                .content(contents)
                .sortBy(new PageResponse.SortBy("entryDate", "desc"))
                .totalPages(warehousesPage.getTotalPages())
                .totalElements(warehousesPage.getTotalElements())
                .build();
    }

    @Override
    public void update(UpdateWarehouseRequest updateRequest) throws BeautyBoxException {
        Warehouse warehouse = warehouseRepository.findById(updateRequest.getId()).orElseThrow(
                () -> new RuntimeException("Không tồn tại kho hàng")
        );
        warehouse.setEntryDate(updateRequest.getEntryDate());
        warehouse.setEntryPlace(updateRequest.getEntryPlace());
        warehouse.setEntryPhoneNumber(updateRequest.getEntryPhoneNumber());
        warehouse.setEntryPrice(updateRequest.getEntryPrice());
        warehouse.setEntryQuantity(updateRequest.getEntryQuantity());
        warehouseRepository.save(warehouse);
        this.updateStock(warehouse.getProductDetail());
    }

    @Override
    public void delete(String warehouseId) throws BeautyBoxException {
        Warehouse warehouse = warehouseRepository.findById(warehouseId).orElseThrow(
                () -> new RuntimeException("Không tồn tại kho hàng")
        );
        warehouseRepository.delete(warehouse);
        this.updateStock(warehouse.getProductDetail());
    }
}
