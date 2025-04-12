package org.beautybox.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateProductDetailRequest {
    @NotBlank(message = "Name field is not blank")
    String name;
    @NotNull(message = "Price filed is not null")
            @Min(value = 0, message = "Price must be greater than 0")
    Long price;
    @Min(value = 0, message = "Discount must be greater than 0")
    Integer discount;
    @NotNull(message = "Stock filed is not null")
            @Min(value = 1, message = "Stock must be greater than 1")
    Integer stock;
    @NotBlank(message = "Product field is not blank")
    String productId;
    MultipartFile image;
}