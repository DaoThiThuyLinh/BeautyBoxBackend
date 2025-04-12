package org.beautybox.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ApiResponse {
    int code;
    String message;
    Object data;

    public static ApiResponse success(String message){
        return ApiResponse.builder()
                .code(200)
                .message(message)
                .build();
    }

    public static ApiResponse success(String message, Object data){
        return ApiResponse.builder()
                .code(200)
                .message(message)
                .data(data)
                .build();
    }

    public static ApiResponse error(String message) {
        return ApiResponse.builder()
                .code(400)
                .message(message)
                .build();
    }
    public static ApiResponse error(int code, String message) {
        return ApiResponse.builder()
                .code(code)
                .message(message)
                .build();
    }
}
