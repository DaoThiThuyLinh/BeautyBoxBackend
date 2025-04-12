package org.beautybox.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Warehouse extends BaseEntity {
    @Id
        @GeneratedValue(strategy = GenerationType.UUID)
            @Column(length = 36)
    String id;
    LocalDate entryDate;
    @Min(value = 0, message = "Entry price must be greater than 0")
    Long entryPrice;
    @Column(columnDefinition = "varchar(12)")
    String entryPhoneNumber;
    String entryPlace;
}
