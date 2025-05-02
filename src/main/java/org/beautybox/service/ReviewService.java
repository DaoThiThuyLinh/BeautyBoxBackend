package org.beautybox.service;

import org.beautybox.entity.User;
import org.beautybox.exception.BeautyBoxException;
import org.beautybox.request.ReplyRequest;
import org.beautybox.request.ReviewRequest;
import org.beautybox.request.UpdateReviewRequest;
import org.springframework.stereotype.Service;

@Service
public interface ReviewService {
    void addReview(ReviewRequest reviewRequest, User user) throws BeautyBoxException;
    void replyReview(ReplyRequest replyRequest, User user) throws BeautyBoxException;
    void updateReview(UpdateReviewRequest updateRequest, User user) throws BeautyBoxException;
}
