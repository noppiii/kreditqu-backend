package com.creditqu.card_product_service.repository;

import com.creditqu.card_product_service.entity.ProductFeature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductFeatureRepository extends JpaRepository<ProductFeature, Long> {
    List<ProductFeature> findByProductId(Long productId);
    List<ProductFeature> findByProduct_ProductCode(String productCode);
}
