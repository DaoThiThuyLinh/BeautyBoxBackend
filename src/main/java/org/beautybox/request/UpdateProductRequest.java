package org.beautybox.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateProductRequest {
    @NotBlank(message = "Chưa lựa chọn sản phẩm cần sửa")
    String productId;
    @NotBlank(message = "Không được bỏ trống tên sản phẩm")
    @Size(min = 2, message = "Tên sản phẩm tối thiểu 2 kí tự")
    String name;
    String description;
    @NotBlank(message = "Không được bỏ trống thể loại của sản phẩm")
    String categoryId;
    @NotBlank(message = "Không được bỏ trống thương hiệu")
    String brandId;
}
