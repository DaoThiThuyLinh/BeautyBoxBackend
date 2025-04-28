package org.beautybox.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDetailResponse extends UserResponse {
    long totalRevenue;
    long totalOrder;
    LocalDateTime createdAt;
}
