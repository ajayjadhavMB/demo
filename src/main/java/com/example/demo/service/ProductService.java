package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.request.CreateProductRequest;
import com.example.demo.dto.request.UpdateProductRequest;
import com.example.demo.dto.response.PageResponse;
import com.example.demo.dto.response.ProductResponse;
import com.example.demo.enums.ProductStatus;

public interface ProductService {
    ProductResponse create(CreateProductRequest request);

    List<ProductResponse> createMultiple(List<CreateProductRequest> request);

    ProductResponse getById(Long id);

    PageResponse<ProductResponse> search(String keyword, ProductStatus status, int page, int size);

    ProductResponse update(Long id, UpdateProductRequest request);

    void delete(Long id);
}
