package org.danilskryl.restapi.service.impl;

import org.danilskryl.restapi.dto.ProductDto;
import org.danilskryl.restapi.mapper.Mapper;
import org.danilskryl.restapi.mapper.ProductMapper;
import org.danilskryl.restapi.model.Product;
import org.danilskryl.restapi.repository.ProductRepository;
import org.danilskryl.restapi.repository.impl.ProductRepositoryImpl;
import org.danilskryl.restapi.service.ProductService;

import java.util.List;

public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final Mapper<Product, ProductDto> mapper;

    public ProductServiceImpl() {
        productRepository = new ProductRepositoryImpl();
        mapper = new ProductMapper();
    }

    public ProductServiceImpl(ProductRepository productRepository, Mapper<Product, ProductDto> mapper) {
        this.productRepository = productRepository;
        this.mapper = mapper;
    }

    @Override
    public List<ProductDto> getAll() {
        List<Product> productList = productRepository.getAll();
        return productList.stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public ProductDto getById(Long id) {
        Product product = productRepository.getById(id);
        return mapper.toDto(product);
    }

    @Override
    public ProductDto save(ProductDto productDto) {
        Product product = mapper.fromDto(productDto);
        Product savedProduct = productRepository.save(product);
        return mapper.toDto(savedProduct);
    }

    @Override
    public ProductDto update(ProductDto productDto) {
        Product product = mapper.fromDto(productDto);
        Product updatedProduct = productRepository.update(product);
        return mapper.toDto(updatedProduct);
    }

    @Override
    public boolean remove(Long id) {
        return productRepository.remove(id);
    }
}
