package com.example.demo.dto.request;

import com.example.demo.enums.ProductStatus;
import com.example.demo.validation.ValidSku;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateProductRequest {

    @NotBlank(message = "Name is required")
    @Size(max = 120, message = "Name must be <= 120 characters")
    private String name;

    @Size(max = 500, message = "Description must be <= 500 characters")
    private String description;

    @NotBlank(message = "SKU is required")
    @ValidSku
    private String sku;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private Double price;

    @NotNull(message = "Status is required")
    private ProductStatus status;
}
