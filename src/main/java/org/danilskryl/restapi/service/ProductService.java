package org.danilskryl.restapi.service;

import org.danilskryl.restapi.dto.ProductTo;

import java.util.List;

public interface ProductService {
    List<ProductTo> getAllProducts();

    ProductTo getProductById(Long id);

    ProductTo saveProduct(ProductTo productTo);

    ProductTo updateProduct(ProductTo productTo);

    boolean removeProduct(Long id);
}
