package org.danilskryl.restapi.service;

import org.danilskryl.restapi.dto.ProductDto;

import java.util.List;

public interface ProductService extends BaseService<ProductDto> {
    List<ProductDto> getAll();

    ProductDto getById(Long id);

    ProductDto save(ProductDto productDto);

    ProductDto update(ProductDto productDto);

    boolean remove(Long id);
}
