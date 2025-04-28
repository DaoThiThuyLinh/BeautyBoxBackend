package org.beautybox.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DefaultAddressResponse {
    String id;
    String name;
    String commune;
    String district;
    String province;
    String detailAddress;
    String recipientName;
    String recipientPhoneNumber;
    boolean isDefault;

}
