package org.danilskryl.restapi.service.impl;

import org.danilskryl.restapi.dao.ProductDAO;
import org.danilskryl.restapi.dto.ProductTo;
import org.danilskryl.restapi.mapper.Mapper;
import org.danilskryl.restapi.mapper.ProductMapper;
import org.danilskryl.restapi.model.Product;
import org.danilskryl.restapi.service.ProductService;

import java.util.List;

public class ProductServiceImpl implements ProductService {
    private final ProductDAO productDAO;
    private final Mapper<Product, ProductTo> mapper = new ProductMapper();

    public ProductServiceImpl() {
        productDAO = new ProductDAO();
    }

    @Override
    public List<ProductTo> getAllProducts() {
        return productDAO.getAllProducts().stream().map(mapper::toDto).toList();
    }

    @Override
    public ProductTo getProductById(Long id) {
        return mapper.toDto(productDAO.getProductById(id));
    }

    @Override
    public ProductTo saveProduct(ProductTo productTo) {
        Product product = mapper.toEntity(productTo);
        return mapper.toDto(productDAO.saveProduct(product));
    }

    @Override
    public ProductTo updateProduct(ProductTo productTo) {
        Product product = mapper.toEntity(productTo);
        return mapper.toDto(productDAO.updateProduct(product));
    }

    @Override
    public boolean removeProduct(Long id) {
        return productDAO.removeProduct(id);
    }
}
