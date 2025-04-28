package org.beautybox.mapper;

import org.beautybox.entity.DefaultAddress;
import org.beautybox.request.CreateDefaultAddressRequest;
import org.beautybox.request.UpdateDefaultAddressRequest;
import org.beautybox.response.DefaultAddressResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring")
public abstract class DefaultAddressMapper {
    public abstract DefaultAddress toDefaultAddress(CreateDefaultAddressRequest request);

    public abstract void update(@MappingTarget DefaultAddress defaultAddress, UpdateDefaultAddressRequest request);

    public abstract DefaultAddressResponse toResponse(DefaultAddress defaultAddress);
}
