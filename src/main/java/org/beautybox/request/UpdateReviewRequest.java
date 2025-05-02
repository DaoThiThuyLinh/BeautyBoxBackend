package org.beautybox.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateReviewRequest {
    @NotBlank(message = "Bạn cần lựa chọn đánh giá để sửa")
    String reviewId;

    @NotNull(message = "Rating không được bỏ trống")
    @Min(value = 0, message = "Số sao đánh giá chỉ từ 0 đến 5")
    @Max(value = 5, message = "Số sao đánh giá chỉ từ 0 đến 5")
    Integer rating;
    String comment;
}
