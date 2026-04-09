package com.example.demo.dto.response;

import com.example.demo.enums.ProductStatus;
import java.time.Instant;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private String sku;
    private Double price;
    private ProductStatus status;
    private Instant createdAt;
    private Instant updatedAt;
}
