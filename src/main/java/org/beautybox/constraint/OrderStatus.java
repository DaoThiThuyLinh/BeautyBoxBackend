package org.beautybox.constraint;

public class OrderStatus {
    public static final int PENDING_CONFIRMATION = 1; // Chờ xác nhận
    public static final int PREPARING_FOR_DELIVERY = 2; //Đang chuẩn bị cho đơn vị vận chuyển
    public static final int IN_TRANSIT = 3; // Đang giao hàng
    public static final int DELIVERED = 4; // Đã giao hàng
    public static final int CANCELLED = 5; // Đã huỷ
    public static final int REJECTED = 6; //  Từ chối nhận hàng
    public static final int AWAITING_PAYMENT = 7; // Chờ thanh toán qua VNPay
}
