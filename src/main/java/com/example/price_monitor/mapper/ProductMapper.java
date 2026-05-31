package com.example.price_monitor.mapper;

import com.example.price_monitor.dto.product.ProductRequestDto;
import com.example.price_monitor.dto.product.ProductResponseDto;
import com.example.price_monitor.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toEntity(ProductRequestDto dto);

    ProductResponseDto toResponse(Product entity);
}
