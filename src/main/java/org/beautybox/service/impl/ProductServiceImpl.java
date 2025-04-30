package org.beautybox.service.impl;

import com.cloudinary.Cloudinary;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.beautybox.entity.*;
import org.beautybox.exception.BeautyBoxException;
import org.beautybox.exception.ErrorDetail;
import org.beautybox.mapper.ProductMapper;
import org.beautybox.repository.*;
import org.beautybox.request.CreateProductDetailRequest;
import org.beautybox.request.CreateProductRequest;
import org.beautybox.request.UpdateProductDetailRequest;
import org.beautybox.request.UpdateProductRequest;
import org.beautybox.response.PageResponse;
import org.beautybox.response.ProductResponse;
import org.beautybox.service.ProductService;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    final Cloudinary cloudinary;
    final CategoryRepository categoryRepository;
    final ProductRepository productRepository;
    final BrandRepository brandRepository;
    final ImageRepository imageRepository;
    final ProductDetailRepository productDetailRepository;
    final EntityManager entityManager;
    private final ProductMapper productMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(CreateProductRequest productRequest) throws BeautyBoxException {
        Brand brand = brandRepository.findById(productRequest.getBrandId()).orElseThrow(
                () -> new BeautyBoxException(ErrorDetail.ERR_BRAND_NOT_EXISTED)
        );
        Category category = categoryRepository.findById(productRequest.getCategoryId()).orElseThrow(
                () -> new BeautyBoxException(ErrorDetail.ERR_CATEGORY_NOT_EXISTED)
        );
        List<String> imageUrls = new ArrayList<>();
        for(var item : productRequest.getImages()){
            imageUrls.add(this.getImageUrl(item));
        }
        Product product = productMapper.toProduct(productRequest);
        product.setBrand(brand);
        product.setCategory(category);
        productRepository.save(product);
        productRepository.flush();
        for(String imageUrl : imageUrls){
            Image image = new Image();
            image.setUrl(imageUrl);
            image.setProduct(product);
            imageRepository.save(image);
        }
    }

    @Override
    public int addNewImage(String productId, List<MultipartFile> images) throws BeautyBoxException {
        int count = 0;
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new BeautyBoxException(ErrorDetail.ERR_PRODUCT_NOT_EXISTED)
        );
        for(var item : images){
            String url = this.getImageUrl(item);
            if(url == null)
                continue;
            count++;
            Image image = new Image();
            image.setUrl(url);
            image.setProduct(product);
            imageRepository.save(image);
        }
        return count;
    }

    private String getImageUrl(MultipartFile file) throws BeautyBoxException {
        try {
            if(file == null || file.isEmpty()){
                return null;
            }
            var response = cloudinary.uploader().upload(file.getBytes(), Map.of());
            return response.get("url").toString();
        }catch (Exception e){
            throw new BeautyBoxException(ErrorDetail.ERR_WHILE_UPLOAD);
        }
    }

    @Override
    public void updateProduct(UpdateProductRequest updateRequest) throws BeautyBoxException {
        Product product = productRepository.findById(updateRequest.getProductId()).orElseThrow(
                () -> new BeautyBoxException(ErrorDetail.ERR_PRODUCT_NOT_EXISTED)
        );
        if(product.getCategory() == null || !product.getCategory().getId().equals(updateRequest.getCategoryId())){
            Category category = categoryRepository.findById(updateRequest.getCategoryId()).orElseThrow(
                    () -> new BeautyBoxException(ErrorDetail.ERR_CATEGORY_NOT_EXISTED)
            );
            product.setCategory(category);
        }
        if(product.getBrand() == null || !product.getBrand().getId().equals(updateRequest.getBrandId())){
            Brand brand = brandRepository.findById(updateRequest.getBrandId()).orElseThrow(
                    () -> new BeautyBoxException(ErrorDetail.ERR_BRAND_NOT_EXISTED)
            );
            product.setBrand(brand);
        }
        product.setName(updateRequest.getName());
        product.setDescription(updateRequest.getDescription());
        productRepository.save(product);
    }

    @Override
    public String addProductDetail(CreateProductDetailRequest productDetailRequest) throws BeautyBoxException {
        Product product = productRepository.findById(productDetailRequest.getProductId()).orElseThrow(
                () -> new BeautyBoxException(ErrorDetail.ERR_PRODUCT_NOT_EXISTED)
        );
        String imageUrl = this.getImageUrl(productDetailRequest.getImage());
        ProductDetail productDetail = productMapper.toProductDetail(productDetailRequest);
        productDetail.setImageUrl(imageUrl);
        productDetail.setProduct(product);
        productDetailRepository.save(productDetail);
        return productDetail.getId();
    }

    @Override
    public void updateProductDetail(UpdateProductDetailRequest updateRequest) throws BeautyBoxException {
        ProductDetail productDetail = productDetailRepository.findById(updateRequest.getId()).orElseThrow(
                () -> new BeautyBoxException(ErrorDetail.ERR_PRODUCT_NOT_EXISTED)
        );
        productDetail.setName(updateRequest.getName());
        productDetail.setDiscount(updateRequest.getDiscount());
        productDetail.setPrice(updateRequest.getPrice());
        productDetail.setDescription(updateRequest.getDescription());
        productDetailRepository.save(productDetail);
    }

    @Override
    public void deleteProductDetail(String id) throws BeautyBoxException{
        ProductDetail productDetail = productDetailRepository.findById(id).orElseThrow(
                () -> new BeautyBoxException(ErrorDetail.ERR_PRODUCT_NOT_EXISTED)
        );
        productDetail.setIsEnabled(false);
        productDetailRepository.save(productDetail);
    }

    @Override
    public void deleteProduct(String productId) throws BeautyBoxException {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new BeautyBoxException(ErrorDetail.ERR_PRODUCT_NOT_EXISTED)
        );
        product.setIsEnabled(false);
        for(var detail : product.getProductDetails()){
            detail.setIsEnabled(false);
        }
        productRepository.save(product);
    }

    @Override
    public void deleteImage(String imageId) throws BeautyBoxException {
        Image image = imageRepository.findById(imageId).orElseThrow(
                () -> new BeautyBoxException(ErrorDetail.ERR_IMAGE_NOT_EXISTED)
        );
        imageRepository.delete(image);
    }

    @Override
    public PageResponse<?> filterProduct(String value, String category, String brand, long minPrice, long maxPrice, int pageIndex, int pageSize, String orderBy, String direction) {
        SearchSession searchSession = Search.session(entityManager);
        SearchResult<Product> searchResult = searchSession.search(Product.class)
                .where(t -> {
                            var bool = t.bool();
                            bool.must(
                                    t.bool()
                                            .should(t.not(t.exists().field("productDetails")))
                                            .should(t.range().field("productDetails.price").between(minPrice, maxPrice))
                            );
                            bool.must(t.match().field("isEnabled").matching(true));
                            if(value != null && !value.isBlank()){
                                bool.must(t.phrase().fields("name", "description", "productDetails.name", "productDetails.description").matching(value).slop(2));
                            }
                            if(category != null && !category.isBlank()){
                                bool.must(t.match().field("category.id").matching(category));
                            }
                            if(brand != null && !brand.isBlank()){
                                bool.must(t.match().field("brand.id").matching(brand));
                            }
                            return bool;
                        }
                ).sort(t -> {
                    var sort = t.composite();
                    sort.add(direction.equals("asc") ? t.field(orderBy).asc() : t.field(orderBy).desc());
                    return sort;
                }).fetch(pageIndex-1, pageSize);

        List<Product> products = searchResult.hits();
        return PageResponse.<ProductResponse>builder()
                .pageIndex(pageIndex)
                .pageSize(pageSize)
                .sortBy(new PageResponse.SortBy(orderBy, direction))
                .content(products.stream().map(productMapper::toProductResponse).toList())
                .totalElements(searchResult.total().hitCount())
                .totalPages((searchResult.total().hitCount() + pageSize - 1) / pageSize)
                .build();
    }

    @Override
    public List<String> suggestNameSearch(String value) {
        SearchResult<?> searchResult = Search.session(entityManager)
                .search(Product.class)
                .select(
                        t-> t.highlight("name")
                )
                .where(t -> {
                    var bool = t.bool();
                    bool.must(t.phrase().field("name").matching(value).slop(2));
                    bool.must(t.match().field("isEnabled").matching(true));
                    return bool;
                })
                .highlighter(t-> t.plain().tag("<b>", "</b>"))
                .fetchAll();
        return searchResult.hits().stream().distinct().map(t-> t.toString().substring(1, t.toString().length() - 1)).toList();
    }

    @Override
    public ProductResponse getProductDetail(String productId) throws BeautyBoxException {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new BeautyBoxException(ErrorDetail.ERR_PRODUCT_NOT_EXISTED)
        );
        return productMapper.toProductResponse(product);
    }
}
