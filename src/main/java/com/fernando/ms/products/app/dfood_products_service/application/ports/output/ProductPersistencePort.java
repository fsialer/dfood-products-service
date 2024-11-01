package com.fernando.ms.products.app.dfood_products_service.application.ports.output;

import com.fernando.ms.products.app.dfood_products_service.domain.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductPersistencePort {

    List<Product> findAll();
    Optional<Product> findById(Long id);
    Product save(Product product);
    void delete(Long id);
    List<Product> findByIds(Iterable<Long> ids);
}
