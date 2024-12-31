package com.fernando.ms.products.app.dfood_products_service.application.ports.input;

import com.fernando.ms.products.app.dfood_products_service.domain.model.Product;

import java.util.List;

public interface ProductInputPort {
    List<Product> findAll();
    Product findById(Long id);
    Product save(Product product);
    Product update(Long id,Product product);
    void delete(Long id);
    List<Product> findByIds(Iterable<Long> ids);
    void verifyExistsProductByIds(Iterable<Long> ids);
    Product updateStock(Long id, Integer quantity,String operation);
}
