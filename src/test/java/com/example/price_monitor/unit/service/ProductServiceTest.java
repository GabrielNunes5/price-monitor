package com.example.price_monitor.unit.service;

import com.example.price_monitor.dto.product.ProductRequestDto;
import com.example.price_monitor.dto.product.ProductResponseDto;
import com.example.price_monitor.entity.Product;
import com.example.price_monitor.exceptions.ResourceNotFoundException;
import com.example.price_monitor.mapper.ProductMapper;
import com.example.price_monitor.repository.ProductRepository;
import com.example.price_monitor.service.PriceHistoryService;
import com.example.price_monitor.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private PriceHistoryService priceHistoryService;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    private Product product;

    @BeforeEach
    void setup(){
        product = new Product();
        product.setProductId("ABC123");
        product.setCurrentPrice(BigDecimal.valueOf(850.00));
        product.setTargetPrice(BigDecimal.valueOf(650.00));
        product.setProductStore("Amazon");
        product.setProductName("Celular Samsung Galaxy A17");
        product.setProductUrl("https://www.amazon.com.br/dp/B0FPYV6K68");
        product.setActive(true);
    }

    @Test
    @DisplayName("Deve criar um produto com sucesso")
    void shouldCreateProduct() {
        ProductRequestDto requestDto = new ProductRequestDto();
        Product entity = new Product();
        ProductResponseDto expectedResponse = new ProductResponseDto();
        expectedResponse.setProductId("NovoID");
        expectedResponse.setProductName("NovoProduto");

        when(productMapper.toEntity(requestDto)).thenReturn(entity);
        when(productRepository.save(any(Product.class))).thenReturn(entity);
        when(productMapper.toResponse(entity)).thenReturn(expectedResponse);

        ProductResponseDto result = productService.createProduct(requestDto);

        assertThat(result).isEqualTo(expectedResponse);
        verify(productMapper, times(1)).toEntity(requestDto);
        verify(productRepository, times(1)).save(entity);
        verify(productMapper, times(1)).toResponse(entity);
    }

    @Test
    @DisplayName("Deve retornar todos os produtos")
    void shouldReturnAllProducts() {
        Pageable pageable = Pageable.ofSize(1);

        ProductResponseDto expectedDto = new ProductResponseDto();
        expectedDto.setProductId(product.getProductId());
        expectedDto.setProductName(product.getProductName());

        Page<Product> productPage =
                new PageImpl<>(List.of(product), pageable, 1);

        when(productRepository.findAll(pageable)).thenReturn(productPage);
        when(productMapper.toResponse(product)).thenReturn(expectedDto);

        Page<ProductResponseDto> result =
                productService.findAllProducts(pageable);

        assertThat(result.getContent())
                .hasSize(1)
                .containsExactly(expectedDto);
    }

    @Test
    @DisplayName("Deve retornar página vazia quando não há produtos")
    void shouldReturnEmptyPageWhenNoProductsExist() {
        Pageable pageable = Pageable.ofSize(10);

        when(productRepository.findAll(pageable))
                .thenReturn(Page.empty());

        Page<ProductResponseDto> result =
                productService.findAllProducts(pageable);

        assertThat(result.getContent()).isEmpty();
    }

    @Test
    @DisplayName("Deve retornar um unico produto")
    void shouldReturnOneProduct() {
        ProductResponseDto productDTO= new ProductResponseDto();
        productDTO.setProductId(product.getProductId());
        productDTO.setProductName(product.getProductName());

        when(productRepository.findById(product.getProductId()))
                .thenReturn(Optional.of(product));
        when(productMapper.toResponse(product))
                .thenReturn(productDTO);

        ProductResponseDto result = productService.findProductById("ABC123");
        assertThat(result).isEqualTo(productDTO);
    }

    @Test
    @DisplayName("Deve lançar exceção quando produto não for encontrado por ID")
    void shouldThrowWhenProductNotFoundById() {
        String noExistentId = "notFoundId";

        when(productRepository.findById(noExistentId))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () ->productService.findProductById(noExistentId)
        );

        assertThat(exception.getMessage()).isEqualTo("Produto não encontrado");
    }

    @Test
    @DisplayName("Deve atualizar o preço e registrar o historico")
    void shouldUpdatePriceAndRecordHistory (){
        BigDecimal newPrice = new BigDecimal("750.00");

        when(productRepository.findById(product.getProductId()))
                .thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class)))
                .thenReturn(product);

        productService.registerNewPrice(product.getProductId(), newPrice);

        assertThat(product.getCurrentPrice()).isEqualTo(newPrice);

        verify(productRepository, times(1)).save(product);

        verify(priceHistoryService, times(1))
                .register(product.getProductId(), newPrice);
    }

    @Test
    @DisplayName("Deve lançar exceção quando produto não existir")
    void shouldThrowWhenProductNotFound() {
        String noExistentId = "notFoundId";
        BigDecimal newPrice = new BigDecimal("750.00");

        when(productRepository.findById(noExistentId))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> productService.registerNewPrice(noExistentId, newPrice)
        );

        assertThat(exception.getMessage())
                .isEqualTo("Produto não encontrado");

        verify(productRepository, never()).save(any());
        verify(priceHistoryService, never()).register(any(), any());
    }

    @Test
    @DisplayName("Deve deletar um produto com sucesso")
    void shouldDeleteProduct() {
        when(productRepository.existsById(product.getProductId()))
                .thenReturn(true);

        productService.deleteProduct(product.getProductId());

        verify(productRepository, times(1))
                .deleteById(product.getProductId());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar deletar produto inexistente")
    void shouldThrowWhenDeletingNonExistentProduct() {
        String idInexistente = "ID_QUE_NAO_EXISTE";

        when(productRepository.existsById(idInexistente))
                .thenReturn(false);

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> productService.deleteProduct(idInexistente)
        );

        assertThat(exception.getMessage()).isEqualTo("Produto não encontrado");

        verify(productRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Deve alterar o Status do produto")
    void shouldChangeProductStatus () {
        when(productRepository.findById(product.getProductId()))
                .thenReturn(Optional.of(product));

        productService.changeProductStatus(product.getProductId());

        assertThat(product.getActive()).isFalse();
        verify(productRepository, times(1))
                .save(product);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar alterar produto inexistente")
    void shouldThrowWhenChangeNonExistentProduct(){
        String idInexistente = "ID_QUE_NAO_EXISTE";

        when(productRepository.findById(idInexistente))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> productService.changeProductStatus(idInexistente)
        );

        assertThat(exception.getMessage())
                .isEqualTo("Produto não encontrado");

        verify(productRepository, never()).save(any());
    }
}
