package org.beautybox.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.beautybox.anotation.CurrentUser;
import org.beautybox.entity.User;
import org.beautybox.exception.BeautyBoxException;
import org.beautybox.request.ReplyRequest;
import org.beautybox.request.ReviewRequest;
import org.beautybox.request.UpdateReviewRequest;
import org.beautybox.response.ApiResponse;
import org.beautybox.service.ReviewService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    final ReviewService reviewService;

    @PostMapping("/review")
    public ApiResponse addNewReview(@RequestBody ReviewRequest reviewRequest, @CurrentUser User user) throws BeautyBoxException {
        reviewService.addReview(reviewRequest, user);
        return ApiResponse.success("Đã thêm đánh giá thành công");
    }

    @PostMapping("/admin-api/review/reply")
    public ApiResponse replyComment(@RequestBody ReplyRequest replyRequest,@CurrentUser User user) throws BeautyBoxException {
        reviewService.replyReview(replyRequest, user);
        return ApiResponse.success("Trả lời thành công");
    }

    @PutMapping("/review")
    public ApiResponse updateReview(@RequestBody @Valid UpdateReviewRequest updateRequest, @CurrentUser User user) throws BeautyBoxException {
        reviewService.updateReview(updateRequest, user);
        return ApiResponse.success("Đánh giá đã được sửa");
    }
}
