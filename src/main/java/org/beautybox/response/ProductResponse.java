package org.beautybox.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

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
    double averageRating;
    long totalNumRating;
    long totalSold;
    List<ImageResponse> images;
    List<ProductDetailResponse> details;
}
