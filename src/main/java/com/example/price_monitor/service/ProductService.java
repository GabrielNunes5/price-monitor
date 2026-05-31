package com.example.price_monitor.service;

import com.example.price_monitor.dto.product.ProductRequestDto;
import com.example.price_monitor.dto.product.ProductResponseDto;
import com.example.price_monitor.entity.Product;
import com.example.price_monitor.exceptions.ResourceNotFoundException;
import com.example.price_monitor.mapper.ProductMapper;
import com.example.price_monitor.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public ProductResponseDto createProduct(ProductRequestDto req) {
        Product entity = productMapper.toEntity(req);
        Product saved =  productRepository.save(entity);
        return productMapper.toResponse(saved);
    }

    public Page<ProductResponseDto> findAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(productMapper::toResponse);
    }

    public ProductResponseDto findProductById(String productId) {
        Product entity = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
        return productMapper.toResponse(entity);
    }

    public void deleteProduct(String productId) {
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Produto não encontrado");
        }
        productRepository.deleteById(productId);
    }

}
