package org.beautybox.service.impl;

import lombok.RequiredArgsConstructor;
import org.beautybox.constraint.OrderStatus;
import org.beautybox.entity.OrderProduct;
import org.beautybox.entity.Review;
import org.beautybox.entity.User;
import org.beautybox.exception.BeautyBoxException;
import org.beautybox.exception.ErrorDetail;
import org.beautybox.repository.OrderRepository;
import org.beautybox.repository.ReviewRepository;
import org.beautybox.request.ReviewRequest;
import org.beautybox.service.ReviewService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    final OrderRepository orderRepository;
    final ReviewRepository reviewRepository;

    @Override
    public void addReview(ReviewRequest reviewRequest, User user) throws BeautyBoxException{
        if(reviewRepository.existsByOrderId(reviewRequest.getOrderId())){
            throw new RuntimeException("Đơn hàng này đã được bạn đánh giá trước đó rồi");
        }
        OrderProduct orderProduct = orderRepository.findById(reviewRequest.getOrderId()).orElseThrow(
                () -> new BeautyBoxException(ErrorDetail.ERR_ORDER_NOT_EXISTED)
        );
        if(orderProduct.getStatus() != OrderStatus.DELIVERED){
            throw new RuntimeException("Bạn chỉ có thể thực hiện đánh giá khi đã nhận hàng");
        }
        if(!orderProduct.getUser().getId().equals(user.getId())){
            throw new BeautyBoxException(ErrorDetail.ERR_ORDER_USER_NOT_CORRECT);
        }
        Review review = new Review();
        review.setOder(orderProduct);
        review.setComment(reviewRequest.getComment());
        review.setRating(reviewRequest.getRating());
        review.setUser(user);

        reviewRepository.save(review);
    }
}
