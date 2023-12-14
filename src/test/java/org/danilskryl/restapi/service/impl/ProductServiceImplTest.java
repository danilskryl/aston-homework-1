package org.danilskryl.restapi.service.impl;

import org.danilskryl.restapi.dto.ProductDto;
import org.danilskryl.restapi.mapper.Mapper;
import org.danilskryl.restapi.model.Product;
import org.danilskryl.restapi.repository.impl.ProductRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {
    @Mock
    private ProductRepositoryImpl productRepository;
    @Mock
    private Mapper<Product, ProductDto> mapper;
    private ProductServiceImpl productService;
    private ProductDto expectedProductDto;
    private Product expectedProduct;

    @BeforeEach
    void setup() {
        productService = new ProductServiceImpl(productRepository, mapper);

        expectedProductDto = ProductDto.builder()
                .id(1L)
                .name("Test market")
                .description("Test description")
                .marketId(1L)
                .build();

        expectedProduct = new Product();
        expectedProduct.setId(1L);
        expectedProduct.setName("Test market");
        expectedProduct.setDescription("Test description");
        expectedProduct.setMarketId(1L);
    }

    @Test
    void getMarketTest() {
        when(productRepository.getById(anyLong())).thenReturn(expectedProduct);
        when(productService.getById(anyLong())).thenReturn(expectedProductDto);

        ProductDto actualProductDto = productService.getById(1L);

        verify(mapper).toDto(expectedProduct);
        assertEquals(expectedProductDto, actualProductDto);
    }

    @Test
    void saveMarketTest() {
        when(productRepository.save(any(Product.class))).thenReturn(expectedProduct);
        when(mapper.fromDto(any(ProductDto.class))).thenReturn(expectedProduct);

        productService.save(expectedProductDto);

        verify(mapper).fromDto(expectedProductDto);
        verify(productRepository).save(expectedProduct);
        verify(mapper).toDto(expectedProduct);
    }

    @Test
    void updateMarketTest() {
        when(mapper.fromDto(any(ProductDto.class))).thenReturn(expectedProduct);
        when(productRepository.update(any(Product.class))).thenReturn(expectedProduct);
        when(mapper.toDto(any(Product.class))).thenReturn(expectedProductDto);

        ProductDto actualMarketDto = productService.update(expectedProductDto);

        verify(mapper).fromDto(expectedProductDto);
        verify(productRepository).update(expectedProduct);
        verify(mapper).toDto(expectedProduct);
        assertEquals(expectedProductDto, actualMarketDto);
    }
}