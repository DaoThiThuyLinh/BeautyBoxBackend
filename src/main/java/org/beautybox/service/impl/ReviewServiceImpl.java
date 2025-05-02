package org.beautybox.service.impl;

import lombok.RequiredArgsConstructor;
import org.beautybox.constraint.OrderStatus;
import org.beautybox.entity.OrderItem;
import org.beautybox.entity.Review;
import org.beautybox.entity.User;
import org.beautybox.exception.BeautyBoxException;
import org.beautybox.exception.ErrorDetail;
import org.beautybox.repository.OrderItemRepository;
import org.beautybox.repository.ReviewRepository;
import org.beautybox.request.ReplyRequest;
import org.beautybox.request.ReviewRequest;
import org.beautybox.request.UpdateReviewRequest;
import org.beautybox.service.ReviewService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    final ReviewRepository reviewRepository;
    final OrderItemRepository orderItemRepository;

    @Override
    public void addReview(ReviewRequest reviewRequest, User user) throws BeautyBoxException{
        if(reviewRepository.existsByOrderItemId(reviewRequest.getOrderItemId())){
            throw new RuntimeException("Đơn hàng này đã được bạn đánh giá trước đó rồi");
        }

        OrderItem orderItem = orderItemRepository.findById(reviewRequest.getOrderItemId()).orElseThrow(
                () -> new RuntimeException("Không tồn tại đơn hàng này")
        );

        if(orderItem.getOrder().getStatus() != OrderStatus.DELIVERED){
            throw new RuntimeException("Bạn chỉ có thể thực hiện đánh giá khi đã nhận hàng");
        }
        if(!orderItem.getOrder().getUser().getId().equals(user.getId())){
            throw new BeautyBoxException(ErrorDetail.ERR_ORDER_USER_NOT_CORRECT);
        }
        Review review = new Review();
        review.setOrderItem(orderItem);
        review.setComment(reviewRequest.getComment());
        review.setRating(reviewRequest.getRating());
        review.setUser(user);

        reviewRepository.save(review);
    }

    @Override
    public void replyReview(ReplyRequest replyRequest, User user) throws BeautyBoxException {
        Review review = reviewRepository.findById(replyRequest.getReviewId()).orElseThrow(
                () -> new RuntimeException("Đánh giá không tồn tại")
        );
        Review reply = new Review();
        reply.setOrderItem(null);
        reply.setComment(replyRequest.getComment());
        reply.setRating(0);
        reply.setParentReview(review);
        reply.setUser(user);
        reviewRepository.save(reply);
    }

    @Override
    public void updateReview(UpdateReviewRequest updateRequest, User user) {
        Review review = reviewRepository.findById(updateRequest.getReviewId()).orElseThrow(
                () -> new RuntimeException("Đánh giá không tồn tại")
        );
        if(!review.getUser().getId().equals(user.getId())){
            throw new RuntimeException("Chỉ có thể chỉnh sửa với đánh giá của bạn");
        }
        review.setComment(updateRequest.getComment());
        review.setRating(updateRequest.getRating());
        reviewRepository.save(review);
    }
}
