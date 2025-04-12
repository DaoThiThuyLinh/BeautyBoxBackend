package org.beautybox.exception;

import lombok.Getter;

@Getter
public enum ErrorDetail {
    ERR_USER_EMAIL_EXISTED(400, "Email already exists")
    , ERR_USER_UN_AUTHENTICATE(401, "Incorrect username or password")
    , ERR_PASSWORD_CONFIRM_INCORRECT(400, "Password confirmation incorrect")
    , ERR_CATEGORY_EXISTED(400, "Category already exists")
    , ERR_BRAND_EXISTED(400, "Brand already exists")
    , ERR_BRAND_NOT_EXISTED(400, "Brand does not exists")
    , ERR_CATEGORY_NOT_EXISTED(400, "Category does not exists")
    , ERR_WHILE_UPLOAD(400, "Error uploading image")
    , ERR_PRODUCT_NOT_EXISTED(400, "Product does not exist")
    ;

    private final int code;
    private final String message;

    ErrorDetail(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
