package com.example.price_monitor.controller;

import com.example.price_monitor.dto.product.ProductRequestDto;
import com.example.price_monitor.dto.product.ProductResponseDto;
import com.example.price_monitor.dto.response.ApiResponse;
import com.example.price_monitor.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponseDto>> createProduct(@Valid @RequestBody ProductRequestDto productRequestDto) {
        ProductResponseDto response = productService.createProduct(productRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ProductResponseDto>>> findAllProducts(Pageable pageable) {
        Page<ProductResponseDto> response = productService.findAllProducts(pageable);
        return ResponseEntity.ok(new ApiResponse<>(response));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductResponseDto>> findProductById(@PathVariable String productId) {
        ProductResponseDto response = productService.findProductById(productId);
        return ResponseEntity.ok(new ApiResponse<>(response));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String productId){
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }
}
