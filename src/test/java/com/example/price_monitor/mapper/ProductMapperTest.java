package com.example.price_monitor.mapper;

import com.example.price_monitor.dto.product.ProductRequestDto;
import com.example.price_monitor.dto.product.ProductResponseDto;
import com.example.price_monitor.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductMapperTest {

    private final ProductMapper mapper = Mappers.getMapper(ProductMapper.class);

    @Test
    @DisplayName("toEntity: deve mapear RequestDto para Product corretamente")
    void shouldMapRequestDtoToEntity() {
        ProductRequestDto dto = new ProductRequestDto();
        dto.setProductUrl("https://www.amazon.com.br/dp/B0FPYV6K68");
        dto.setProductName("Celular Samsung Galaxy A17");
        dto.setProductStore("Amazon");
        dto.setTargetPrice(BigDecimal.valueOf(650.00));
        dto.setCurrentPrice(BigDecimal.valueOf(850.00));

        Product entity = mapper.toEntity(dto);

        assertThat(entity).isNotNull();
        assertThat(entity.getProductUrl()).isEqualTo(dto.getProductUrl());
        assertThat(entity.getProductName()).isEqualTo(dto.getProductName());
        assertThat(entity.getProductStore()).isEqualTo(dto.getProductStore());
        assertThat(entity.getTargetPrice()).isEqualTo(dto.getTargetPrice());
        assertThat(entity.getCurrentPrice()).isEqualTo(dto.getCurrentPrice());
    }

    @Test
    @DisplayName("toResponse: deve mapear Product para ResponseDto corretamente")
    void shouldMapEntityToResponseDto() {
        Product entity = new Product();
        entity.setProductId("ABC123");
        entity.setProductUrl("https://www.amazon.com.br/dp/B0FPYV6K68");
        entity.setProductName("Celular Samsung Galaxy A17");
        entity.setProductStore("Amazon");
        entity.setTargetPrice(BigDecimal.valueOf(650.00));
        entity.setCurrentPrice(BigDecimal.valueOf(850.00));
        entity.setActive(true);

        ProductResponseDto dto = mapper.toResponse(entity);

        assertThat(dto).isNotNull();
        assertThat(dto.getProductId()).isEqualTo(entity.getProductId());
        assertThat(dto.getProductUrl()).isEqualTo(entity.getProductUrl());
        assertThat(dto.getProductName()).isEqualTo(entity.getProductName());
        assertThat(dto.getProductStore()).isEqualTo(entity.getProductStore());
        assertThat(dto.getTargetPrice()).isEqualTo(entity.getTargetPrice());
        assertThat(dto.getCurrentPrice()).isEqualTo(entity.getCurrentPrice());
        assertThat(dto.getActive()).isTrue();
    }
}
