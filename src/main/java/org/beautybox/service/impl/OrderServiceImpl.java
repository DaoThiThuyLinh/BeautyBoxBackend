package org.beautybox.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.beautybox.common.NanoId;
import org.beautybox.config.VNPayConfig;
import org.beautybox.constraint.OrderStatus;
import org.beautybox.entity.OrderItem;
import org.beautybox.entity.OrderProduct;
import org.beautybox.entity.ProductDetail;
import org.beautybox.entity.User;
import org.beautybox.exception.BeautyBoxException;
import org.beautybox.exception.ErrorDetail;
import org.beautybox.mapper.OrderMapper;
import org.beautybox.repository.OrderItemRepository;
import org.beautybox.repository.OrderRepository;
import org.beautybox.repository.ProductDetailRepository;
import org.beautybox.repository.RedisRepository;
import org.beautybox.request.OrderRequest;
import org.beautybox.request.UpdateOrderRequest;
import org.beautybox.response.OrderResponse;
import org.beautybox.response.PageResponse;
import org.beautybox.service.OrderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    @Value("${vnpay.return.url}")
    String vnp_ReturnUrl;
    final OrderMapper orderMapper;
    final OrderRepository orderRepository;
    final ProductDetailRepository productDetailRepository;
    final RedisRepository redisRepository;
    final OrderItemRepository orderItemRepository;

    @Override
    @Transactional(rollbackFor = RuntimeException.class )
    public String add(User user, OrderRequest orderRequest, HttpServletRequest request) throws BeautyBoxException {

        OrderProduct orderProduct = orderMapper.toOrder(orderRequest);
        orderProduct.setUser(user);
        orderRepository.save(orderProduct);
        orderRepository.flush();

        if(orderProduct.getPaymentType() == 2) {
            orderProduct.setStatus(OrderStatus.AWAITING_PAYMENT);
        }
        List<OrderRequest.innerRequest> orderItems = orderRequest.getOrderItems();
        List<OrderItem> data = new ArrayList<>();
        for(int i = 0 ; i< orderItems.size() ; i++){
            Optional<ProductDetail> productDetailOptional = productDetailRepository.findById(orderItems.get(i).getProductDetailId());
            if(productDetailOptional.isPresent()){
                ProductDetail productDetail = productDetailOptional.get();
                long totalSold = orderItemRepository.sumByProductDetailId(productDetail.getId());
                if(productDetail.getStock() - totalSold < orderItems.get(i).getQuantity()) {
                    throw new RuntimeException("Sản phẩm '" + productDetail.getProduct().getName() + " - " + productDetail.getName() +  "' trong kho hiện không còn đủ");
                }
                NanoId nanoId = new NanoId();
                OrderItem orderItem = new OrderItem();
                orderItem.setId(nanoId.gen());
                orderItem.setProductId(productDetail.getProduct().getId());
                orderItem.setProductName(productDetail.getProduct().getName());
                orderItem.setDescription(productDetail.getProduct().getDescription());
                orderItem.setProductDetailId(productDetail.getId());
                orderItem.setProductDetailName(productDetail.getName());
                orderItem.setPrice(productDetail.getPrice());
                orderItem.setDiscount(productDetail.getDiscount());
                orderItem.setImageUrl(productDetail.getImageUrl());
                orderItem.setQuantity(orderItems.get(i).getQuantity());
                orderItem.setOrder(orderProduct);
                data.add(orderItem);
                orderItemRepository.save(orderItem);
                orderItemRepository.flush();
            }else{
                throw new RuntimeException("Mặt hàng số " + (i + 1) + " không tồn tại");
            }
        }
        orderProduct.setOrderItems(data);
        if(orderProduct.getPaymentType() == 1) {
            return null;
        }
        return this.payment(orderProduct, request);
    }

    @Override
    public void update(UpdateOrderRequest updateRequest) throws BeautyBoxException {
        OrderProduct order = orderRepository.findById(updateRequest.getOrderId()).orElseThrow(
                () -> new BeautyBoxException(ErrorDetail.ERR_ORDER_NOT_EXISTED)
        );
        if(updateRequest.getOrderCode() != null && !updateRequest.getOrderCode().equals(order.getOrderCode()) && orderRepository.existsByOrderCode(updateRequest.getOrderCode())) {
            throw new RuntimeException("Mã vận chuyển đã tồn tại");
        }
        order.setStatus(updateRequest.getStatus());
        order.setOrderCode(updateRequest.getOrderCode());
        orderRepository.save(order);
    }

    @SneakyThrows
    @Override
    public void cancelOrder(String orderId, User user){
        OrderProduct order = orderRepository.findById(orderId).orElseThrow(
                () -> new BeautyBoxException(ErrorDetail.ERR_ORDER_NOT_EXISTED)
        );
        if(!user.getId().equals(order.getUser().getId())) {
            throw new BeautyBoxException(ErrorDetail.ERR_ORDER_USER_NOT_CORRECT);
        }
        if(order.getPaymentType() == 1){
            if(order.getStatus() == OrderStatus.PENDING_CONFIRMATION) { // Chỉ huỷ đơn hàng có trạng thái chờ xác nhận
                order.setStatus(OrderStatus.CANCELLED);
                orderRepository.save(order);
            }else{
                if(order.getStatus() == OrderStatus.CANCELLED) {
                    throw new RuntimeException("Đơn hàng đã được huỷ trước đó");
                }
                throw new RuntimeException("Vui lòng chỉ huỷ đơn hàng có trạng thái chờ xác nhận");
            }
        }else{
            if(order.getStatus() == OrderStatus.AWAITING_PAYMENT) {
                order.setStatus(OrderStatus.CANCELLED);
                orderRepository.save(order);
            }else{
                if(order.getStatus() == OrderStatus.CANCELLED) {
                    throw new RuntimeException("Đơn hàng đã được huỷ trước đó");
                }
                throw new RuntimeException("Đơn hàng đã thanh toán không thể huỷ");
            }
        }
    }

    @Override
    public String payAgain(String orderId, HttpServletRequest request, User user) throws BeautyBoxException{
        OrderProduct order = orderRepository.findById(orderId).orElseThrow(
                () -> new BeautyBoxException(ErrorDetail.ERR_ORDER_NOT_EXISTED)
        );
        if(order.getStatus() != OrderStatus.AWAITING_PAYMENT){
            throw new BeautyBoxException(ErrorDetail.ERR_JUST_PAY);
        }
        if(!user.getId().equals(order.getUser().getId())) {
            throw new BeautyBoxException(ErrorDetail.ERR_ORDER_USER_NOT_CORRECT);
        }
        LocalDateTime createDate = order.getCreatedAt();
        if(LocalDateTime.now().isAfter(createDate.plusDays(1))) {
            order.setStatus(OrderStatus.CANCELLED);
            throw new BeautyBoxException(ErrorDetail.ERR_ORDER_TIME_VALID);
        }
        return this.payment(order, request);
    }

    @Override
    public String executePaymentResult(Map<String, String> params, HttpServletRequest request) throws BeautyBoxException, UnsupportedEncodingException {
        String value = "";
        List<String> fieldNames = new ArrayList<>(params.keySet());
        Collections.sort(fieldNames);
        Map<String, String> hashData = new HashMap<>();
        for(String fieldName : fieldNames) {
            String fieldValue = params.get(fieldName);
            if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                fieldName = URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString());
                fieldValue = URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString());
                hashData.put(fieldName, fieldValue);
            }
        }
        hashData.remove("vnp_SecureHashType");
        String receivedHash = hashData.remove("vnp_SecureHash");
        String signValue = VNPayConfig.hashAllFields(hashData);
        if (signValue.equals(receivedHash)) {
            String orderId = redisRepository.get(params.get("vnp_TxnRef")).toString();
            OrderProduct order = orderRepository.findById(orderId).orElseThrow(
                    () -> new BeautyBoxException(ErrorDetail.ERR_ORDER_NOT_EXISTED)
            );
            LocalDateTime createdDate = order.getCreatedAt();
            LocalDateTime timeout = createdDate.plusDays(1);
            if(LocalDateTime.now().isAfter(timeout)) {
                throw new BeautyBoxException(ErrorDetail.ERR_ORDER_TIME_VALID);
            }
            String vnp_ResponseCode = params.get("vnp_ResponseCode");
            if(vnp_ResponseCode.equals("00")){
                order.setStatus(OrderStatus.PENDING_CONFIRMATION);
                orderRepository.save(order);
                value = "Thanh toán thành công!";
            }
            if(vnp_ResponseCode.equals("11"))
                value = "Giao dịch không thành công do: Đã hết hạn chờ thanh toán. Xin quý khách vui lòng thực hiện lại giao dịch.";
            if(vnp_ResponseCode.equals("12"))
                value = "Giao dịch không thành công do: Thẻ/Tài khoản của khách hàng bị khóa.";
            if(vnp_ResponseCode.equals("13"))
                value = "Giao dịch không thành công do Quý khách nhập sai mật khẩu xác thực giao dịch (OTP). Xin quý khách vui lòng thực hiện lại giao dịch.";
            if(vnp_ResponseCode.equals("24"))
                value = "Giao dịch không thành công do: Khách hàng hủy giao dịch";
            if(vnp_ResponseCode.equals("51"))
                value = "Giao dịch không thành công do: Tài khoản của quý khách không đủ số dư để thực hiện giao dịch.";
            if(vnp_ResponseCode.equals("65"))
                value = "Giao dịch không thành công do: Tài khoản của Quý khách đã vượt quá hạn mức giao dịch trong ngày.";
            if(vnp_ResponseCode.equals("75"))
                value = "Ngân hàng thanh toán đang bảo trì.";
            if(vnp_ResponseCode.equals("79"))
                value = "Giao dịch không thành công do: KH nhập sai mật khẩu thanh toán quá số lần quy định. Xin quý khách vui lòng thực hiện lại giao dịch";
        }else{
            log.warn("Giao dịch giả mạo từ IP: {}", request.getRemoteAddr());
            value = "Giao dịch không hợp lệ, vui lòng kết nối để biết thêm chi tiết";
        }
        return value;
    }

    @SneakyThrows
    public String payment(OrderProduct orderProduct, HttpServletRequest req) {
        String orderType = "other";
        long totalAmount = 0L;
        for(OrderItem item: orderProduct.getOrderItems()) {
            totalAmount = totalAmount + item.getQuantity() * ( item.getPrice() - item.getPrice() * item.getDiscount() / 100)  * 100;
        }

        String vnp_TxnRef = orderProduct.getId() + LocalDateTime.now();// Khi cần thanh toán lại thì cần tạo mã thanh toán mới
        this.redisRepository.set(vnp_TxnRef, orderProduct.getId()); // Lưu lại giá trị thực tế của OrderId
        String vnp_IpAddr = VNPayConfig.getIpAddress(req);
        String vnp_TmnCode = VNPayConfig.vnp_TmnCode;
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", VNPayConfig.vnp_Version);
        vnp_Params.put("vnp_Command", VNPayConfig.vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(totalAmount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toán đơn hàng " + orderProduct.getId());
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        vnp_Params.put("vnp_OrderType", orderType);


        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 10);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        System.out.println(hashData);

        String queryUrl = query.toString();
        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.secretKey, hashData.toString());
        System.out.println(vnp_SecureHash);
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        return VNPayConfig.vnp_PayUrl + "?" + queryUrl;
    }

    @Override
    public PageResponse<OrderResponse> get(String s, String userId, int pageIndex, int pageSize, int status) {
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<OrderProduct> ordersPage = orderRepository.getOrders(s, userId, status, pageable);
        List<OrderResponse> contents = ordersPage.getContent().stream().map(orderMapper::toResponse).toList();
        return PageResponse.<OrderResponse>builder()
                .pageIndex(pageIndex)
                .pageSize(pageSize)
                .totalPages(ordersPage.getTotalPages())
                .totalElements(ordersPage.getTotalElements())
                .content(contents)
                .sortBy(new PageResponse.SortBy("createdAt", "desc"))
                .build();
    }
}
