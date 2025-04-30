package org.beautybox.mapper;

import org.beautybox.entity.Warehouse;
import org.beautybox.request.CreateWarehouseRequest;
import org.beautybox.response.WarehouseResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class WarehouseMapper {
    public abstract Warehouse toWarehouse(CreateWarehouseRequest createRequest);
    public abstract WarehouseResponse toResponse(Warehouse warehouse);
}
