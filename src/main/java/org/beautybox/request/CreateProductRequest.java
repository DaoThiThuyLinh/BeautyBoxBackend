package org.beautybox.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateProductRequest {
    @NotBlank(message = "Name field cannot be left empty")
        @Size(min = 2, message = "Name field must have at least 2 characters")
    String name;
    String description;
    @NotBlank(message = "Category field cannot be left empty")
    String categoryId;
    @NotBlank(message = "Brand field cannot be left empty")
    String brandId;
    @NotNull(message = "Image filed cannot be null")
    List<MultipartFile> images;
}
