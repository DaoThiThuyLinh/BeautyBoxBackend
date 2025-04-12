package org.beautybox.mapper;

import org.beautybox.entity.Product;
import org.beautybox.entity.ProductDetail;
import org.beautybox.repository.ImageRepository;
import org.beautybox.repository.ProductDetailRepository;
import org.beautybox.repository.ProductRepository;
import org.beautybox.request.CreateProductDetailRequest;
import org.beautybox.request.CreateProductRequest;
import org.beautybox.response.ProductDetailResponse;
import org.beautybox.response.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class ProductMapper {

    @Autowired
    ImageRepository imageRepository;
    @Autowired
    ProductDetailRepository productDetailRepository;
    @Autowired
    private ProductRepository productRepository;

    public abstract Product fromCreateProductRequest(CreateProductRequest request);
    public abstract ProductDetail toProductDetail(CreateProductDetailRequest request);
    @Mappings({
            @Mapping(target = "categoryId", source = "category.id"),
            @Mapping(target = "categoryName", source = "category.name"),
            @Mapping(target = "brandId", source = "brand.id"),
            @Mapping(target = "brandName", source = "brand.name"),
            @Mapping(target = "brandImgUrl", source = "brand.imgUrl"),
            @Mapping(target = "images", expression = "java(imageRepository.findByProductId(product.getId()))"),
            @Mapping(target = "details", expression = "java(this.productDetailResponses(product.getId()))")
        }
    )
    public abstract ProductResponse toProductResponse(Product product);

    protected List<ProductDetailResponse> productDetailResponses(String productId){
        return productDetailRepository.findByProductId(productId).stream().map(this::toProductDetailResponse).toList();
    }

    @Mappings({
            @Mapping(target = "image", source = "imageUrl"),
            @Mapping(target = "status", expression = "java(this.convertStatus(product.getStock()))")
    })
    public abstract ProductDetailResponse toProductDetailResponse(ProductDetail product);

    protected String convertStatus(int stock){
        if(stock > 0){
            return "Còn hàng";
        }
        return "Hết hàng";
    }
}
