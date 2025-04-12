package org.beautybox.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDetailResponse {
    String id;
    String name;
    long price;
    String status;
    int discount;
    int stock;
    String image;
}
