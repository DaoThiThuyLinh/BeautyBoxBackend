package org.beautybox.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateBrandRequest {
    @NotBlank(message = "Brand name field is not blank")
    String name;
    String description;
    MultipartFile image;
}
