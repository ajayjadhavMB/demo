package com.example.demo.service.impl;

import com.example.demo.dto.request.CreateProductRequest;
import com.example.demo.dto.request.UpdateProductRequest;
import com.example.demo.dto.response.PageResponse;
import com.example.demo.dto.response.ProductResponse;
import com.example.demo.entity.Product;
import com.example.demo.enums.ProductStatus;
import com.example.demo.exception.DuplicateResourceException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.ProductService;
import com.example.demo.specification.ProductSpecification;
import com.example.demo.util.PageUtil;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductResponse create(CreateProductRequest request) {
        if (productRepository.existsBySku(request.getSku())) {
            throw new DuplicateResourceException("Product with SKU already exists: " + request.getSku());
        }

        Product saved = productRepository.save(productMapper.toEntity(request));
        return productMapper.toResponse(saved);
    }

    @Override
    public ProductResponse getById(Long id) {
        Product product = findProduct(id);
        return productMapper.toResponse(product);
    }

    @Override
    public PageResponse<ProductResponse> search(String keyword, ProductStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Specification<Product> spec = Specification
                .where(ProductSpecification.nameContains(keyword))
                .and(ProductSpecification.hasStatus(status));

        Page<ProductResponse> responsePage = productRepository
                .findAll(spec, pageable)
                .map(productMapper::toResponse);

        return PageUtil.fromPage(responsePage);
    }

    @Override
    public ProductResponse update(Long id, UpdateProductRequest request) {
        Product product = findProduct(id);
        productMapper.updateEntity(request, product);
        Product updated = productRepository.save(product);
        return productMapper.toResponse(updated);
    }

    @Override
    public void delete(Long id) {
        Product product = findProduct(id);
        productRepository.delete(product);
    }

    private Product findProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    @Override
    public List<ProductResponse> createMultiple(List<CreateProductRequest> request) {
        // Check for duplicate SKUs in the request
        for (CreateProductRequest req : request) {
            if (productRepository.existsBySku(req.getSku())) {
                throw new DuplicateResourceException("Product with SKU already exists: " + req.getSku());
            }
        }

        List<Product> products = productMapper.toEntityList(request);
        List<Product> savedProducts = productRepository.saveAll(products);
        return productMapper.toResponseList(savedProducts);
    }
}
