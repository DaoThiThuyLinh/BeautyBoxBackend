package org.beautybox.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewRequest {
    @Min(value = 0, message = "Số sao đánh giá chỉ từ 0 đến 5")
    @Max(value = 5, message = "Số sao đánh giá chỉ từ 0 đến 5")
    Integer rating;
    String comment;
    @NotBlank(message = "Bạn cần lựa chọn sản phẩm để thực hiện đánh giá")
    String orderItemId;
}
