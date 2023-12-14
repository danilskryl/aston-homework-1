package org.danilskryl.restapi.mapper;

import org.danilskryl.restapi.dto.ProductDto;
import org.danilskryl.restapi.model.Product;

public class ProductMapper implements Mapper<Product, ProductDto> {

    @Override
    public Product fromDto(ProductDto dto) {
        Product product = new Product();
        product.setId(dto.getId());
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setMarketId(dto.getMarketId());

        return product;
    }

    @Override
    public ProductDto toDto(Product market) {
        return ProductDto.builder()
                .id(market.getId())
                .name(market.getName())
                .description(market.getDescription())
                .marketId(market.getMarketId())
                .build();
    }
}
