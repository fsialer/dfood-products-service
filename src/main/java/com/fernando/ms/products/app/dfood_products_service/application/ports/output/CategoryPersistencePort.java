package com.fernando.ms.products.app.dfood_products_service.application.ports.output;

import com.fernando.ms.products.app.dfood_products_service.domain.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryPersistencePort {
    List<Category> findAll();
    Optional<Category> findById(Long id);
    Category save(Category category);
    void delete(Long id);
}
