package org.beautybox.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartResponse {
    String id;
    ProductDetailResponse productDetail;
    int quantity;
    String messageStatus;
    Boolean isEnabled;
    LocalDateTime createdAt;
}
