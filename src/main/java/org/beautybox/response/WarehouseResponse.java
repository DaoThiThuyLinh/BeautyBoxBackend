package org.beautybox.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WarehouseResponse {
    String id;
    LocalDate entryDate;
    Long entryPrice;
    String entryPhoneNumber;
    String entryPlace;
    Integer entryQuantity;
}
