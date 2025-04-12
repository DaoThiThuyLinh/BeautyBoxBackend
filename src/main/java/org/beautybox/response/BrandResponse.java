package org.beautybox.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BrandResponse {
    String id;
    String name;
    String imgUrl;
    String description;
    int totalProduct;
}
