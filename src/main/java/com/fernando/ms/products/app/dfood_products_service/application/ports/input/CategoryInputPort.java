package com.fernando.ms.products.app.dfood_products_service.application.ports.input;

import com.fernando.ms.products.app.dfood_products_service.domain.model.Category;

import java.util.List;

public interface CategoryInputPort {
    List<Category> findAll();
    Category findById(Long id);
    Category save(Category category);
    Category update(Long id,Category category);
    void delete(Long id);
}
