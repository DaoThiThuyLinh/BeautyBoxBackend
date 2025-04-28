package org.beautybox.mapper;

import org.beautybox.entity.DefaultAddress;
import org.beautybox.request.CreateDefaultAddressRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class DefaultAddressMapper {
    public abstract DefaultAddress toDefaultAddress(CreateDefaultAddressRequest request);
}
