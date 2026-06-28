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

import java.math.BigDecimal;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final PriceHistoryService priceHistoryService;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper, PriceHistoryService priceHistoryService) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.priceHistoryService = priceHistoryService;
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

    public void changeProductStatus(String productId){
        Product entity = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
        entity.setActive(!entity.getActive());
        productRepository.save(entity);
    }

    public void registerNewPrice(String productId, BigDecimal newPrice) {
        Product entity = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        entity.setCurrentPrice(newPrice);
        Product saved = productRepository.save(entity);

        priceHistoryService.register(productId, newPrice);

        productMapper.toResponse(saved);
    }

    public void deleteProduct(String productId) {
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Produto não encontrado");
        }
        productRepository.deleteById(productId);
    }

}
