package org.beautybox.exception;

import lombok.Getter;

@Getter
public enum ErrorDetail {
    ERR_USER_EMAIL_EXISTED(400, "Email already exists")
    , ERR_USER_UN_AUTHENTICATE(401, "Incorrect username or password")
    , ERR_USER_NOT_EXISTED(404, "Người dùng không tồn tại")
    , ERR_PASSWORD_CONFIRM_INCORRECT(400, "Password confirmation incorrect")
    , ERR_CATEGORY_EXISTED(400, "Category already exists")
    , ERR_BRAND_EXISTED(400, "Brand already exists")
    , ERR_BRAND_NOT_EXISTED(400, "Brand does not exists")
    , ERR_CATEGORY_NOT_EXISTED(400, "Category does not exists")
    , ERR_WHILE_UPLOAD(400, "Error uploading image")
    , ERR_PRODUCT_NOT_EXISTED(400, "Sản phẩm không tồn tại")
    , ERR_CART_EXISTED(400, "Giỏ hàng đã tồn tại")
    , ERR_ORDER_NOT_EXISTED(400, "Đơn hàng không tồn tại")
    , ERR_ORDER_TIME_VALID(400, "Đơn hàng đã qúa thời gian thanh toán") //24h
    , ERR_ORDER(400, "Số lượng sản phẩm hiện tại trong kho không đủ")
    , ERR_ORDER_USER_NOT_CORRECT(400, "Thao tác này chỉ có với đơn hàng của bạn")
    , ERR_JUST_PAY(400, "Chỉ thực hiện thanh toán lại với các đơn hàng có trạng thái chờ thanh toán")
    , ERR_IMAGE_NOT_EXISTED(400, "Không tìm thấy thông tin ảnh")
    ;

    private final int code;
    private final String message;

    ErrorDetail(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
