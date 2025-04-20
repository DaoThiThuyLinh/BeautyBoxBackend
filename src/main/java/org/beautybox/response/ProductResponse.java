package org.beautybox.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {
    String id;
    String name;
    String description;
    String categoryId;
    String categoryName;
    String brandId;
    String brandName;
    String brandImgUrl;
    long totalSold;
    List<ProductDetailResponse> details;
    List<ImageResponse> images;
    Map<String, Object> reviewsResponse;
}
