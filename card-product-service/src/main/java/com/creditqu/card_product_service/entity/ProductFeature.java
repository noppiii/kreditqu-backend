package com.creditqu.card_product_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "product_features")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductFeature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private CardProduct product;

    @Column(name = "feature_code", nullable = false, length = 50)
    private String featureCode;

    @Column(name = "feature_value", length = 255)
    private String featureValue;

    @Column(columnDefinition = "TEXT")
    private String description;
}
