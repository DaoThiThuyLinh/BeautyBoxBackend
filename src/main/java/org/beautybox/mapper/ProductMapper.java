package org.beautybox.mapper;

import org.beautybox.common.NanoId;
import org.beautybox.entity.Image;
import org.beautybox.entity.Product;
import org.beautybox.entity.ProductDetail;
import org.beautybox.entity.Review;
import org.beautybox.repository.*;
import org.beautybox.request.CreateProductDetailRequest;
import org.beautybox.request.CreateProductRequest;
import org.beautybox.response.ImageResponse;
import org.beautybox.response.ProductDetailResponse;
import org.beautybox.response.ProductResponse;
import org.beautybox.response.ReviewResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper(componentModel = "spring")
public abstract class ProductMapper {

    @Autowired
    ImageRepository imageRepository;
    @Autowired
    ProductDetailRepository productDetailRepository;
    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    OrderItemRepository orderItemRepository;
    NanoId nanoId = new NanoId();


    @Mappings({
            @Mapping(target = "isEnabled", constant = "true"),
            @Mapping(target = "id", expression = "java(nanoId.gen())")
        }
    )
    public abstract Product toProduct(CreateProductRequest request);

    @Mapping(target = "isEnabled", constant = "true")
    public abstract ProductDetail toProductDetail(CreateProductDetailRequest request);


    @Mappings({
            @Mapping(target = "categoryId", source = "category.id"),
            @Mapping(target = "categoryName", source = "category.name"),
            @Mapping(target = "brandId", source = "brand.id"),
            @Mapping(target = "brandName", source = "brand.name"),
            @Mapping(target = "brandImgUrl", source = "brand.imgUrl"),
            @Mapping(target = "reviewsResponse", expression = "java(this.getReviews(product.getId()))"),
            @Mapping(target = "images", expression = "java(this.getProductImages(product.getId()))"),
            @Mapping(target = "details", expression = "java(this.productDetailResponses(product.getId()))"),
            @Mapping(target = "totalSold", expression = "java(this.getTotalSold(product.getId()))")
        }
    )
    public abstract ProductResponse toProductResponse(Product product);


    @Mappings({
            @Mapping(target = "image", source = "imageUrl"),
            @Mapping(target = "status", expression = "java(this.convertStatus(product.getStock()))"),
            @Mapping(target = "newPrice", expression = "java(this.getNewPrice(product.getPrice(), product.getDiscount()))"),
            @Mapping(target = "totalSold", expression = "java(this.getTotalSoldProductDetail(product.getId()))")
    })
    public abstract ProductDetailResponse toProductDetailResponse(ProductDetail product);


    protected Map<String, Object> getReviews(String productId){
        Map<String, Object> response = new HashMap<>();
        List<ReviewResponse> reviewResponses = reviewRepository.findByProductId(productId).stream().map(t -> {
            ReviewResponse review = new ReviewResponse();
            review.setId(t.getId());
            review.setCreatedDate(t.getCreatedAt());
            review.setUserName(t.getUser().getName());
            review.setRating(t.getRating());
            review.setComment(t.getComment());
            List<ReviewResponse.childComment> childComments = new ArrayList<>();
            for(Review x : reviewRepository.getByReviewId(t.getId())){
                ReviewResponse.childComment childComment = new ReviewResponse.childComment();
                childComment.setId(x.getId());
                childComment.setCreatedDate(x.getCreatedAt());
                childComment.setUserName(x.getUser().getName());
                childComment.setComment(x.getComment());
                childComments.add(childComment);
            }
            review.setReplies(childComments);
            return review;
        }).toList();
        Map<Integer, Long> details = new HashMap<>();
        details.put(1, reviewRepository.countReviewByRatingAndProductId(1, productId));
        details.put(2, reviewRepository.countReviewByRatingAndProductId(2, productId));
        details.put(3, reviewRepository.countReviewByRatingAndProductId(3, productId));
        details.put(4, reviewRepository.countReviewByRatingAndProductId(4, productId));
        details.put(5, reviewRepository.countReviewByRatingAndProductId(5, productId));
        response.put("details", details);
        response.put("reviews", reviewResponses);
        response.put("totalNumRating", reviewResponses.size());

        long sumRate = 0;
        for(ReviewResponse review : reviewResponses){
            sumRate += review.getRating();
        }
        response.put("averageRating", (double) (sumRate / (reviewResponses.isEmpty() ? 1: reviewResponses.size() )));
        return response;
    }

    protected List<ImageResponse> getProductImages(String productId) {
        List<Image> images = imageRepository.findByProductId(productId);
        return images.stream().map(t -> {
            ImageResponse imageResponse = new ImageResponse();
            imageResponse.setId(t.getId());
            imageResponse.setImage(t.getUrl());
            return imageResponse;
        }).toList();
    }

    protected long getTotalSoldProductDetail(String productDetailId){
        return orderItemRepository.sumByProductDetailId(productDetailId);
    }

    protected long getTotalSold(String productId) {
        return orderItemRepository.sumByProductId(productId);
    }

    protected List<ProductDetailResponse> productDetailResponses(String productId){
        return productDetailRepository.findByProductId(productId).stream()
                .filter(ProductDetail::getIsEnabled)
                .map(this::toProductDetailResponse)
                .toList();
    }

    protected long getNewPrice(long price, int discount){
        return price - (price * discount) / 100;
    }

    protected String convertStatus(int stock){
        if(stock > 0){
            return "Còn hàng";
        }
        return "Hết hàng";
    }
}
