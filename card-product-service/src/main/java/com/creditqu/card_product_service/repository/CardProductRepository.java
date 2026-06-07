package com.creditqu.card_product_service.repository;

import com.creditqu.card_product_service.entity.CardProduct;
import com.creditqu.common_module.constant.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface CardProductRepository extends JpaRepository<CardProduct, Long> {
    Optional<CardProduct> findByProductCode(String productCode);
    List<CardProduct> findByStatus(ProductStatus status);
    List<CardProduct> findByMinIncomeLessThanEqualAndStatus(BigDecimal income, ProductStatus status);
}
