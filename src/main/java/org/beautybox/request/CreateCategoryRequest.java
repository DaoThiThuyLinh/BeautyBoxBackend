package org.beautybox.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateCategoryRequest {
    @JsonProperty(value = "categoryName")
            @NotBlank(message = "Category name field is not blank")
    String name;
}
