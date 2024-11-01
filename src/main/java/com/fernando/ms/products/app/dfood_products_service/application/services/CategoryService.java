package com.fernando.ms.products.app.dfood_products_service.application.services;

import com.fernando.ms.products.app.dfood_products_service.application.ports.input.CategoryInputPort;
import com.fernando.ms.products.app.dfood_products_service.application.ports.output.CategoryPersistencePort;
import com.fernando.ms.products.app.dfood_products_service.domain.exceptions.CategoryNotFoundException;
import com.fernando.ms.products.app.dfood_products_service.domain.model.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements CategoryInputPort {

    private final CategoryPersistencePort categoryPersistencePort;

    @Override
    public List<Category> findAll() {
        return categoryPersistencePort.findAll();
    }

    @Override
    public Category findById(Long id) {
        return categoryPersistencePort.findById(id).orElseThrow(CategoryNotFoundException::new);
    }

    @Override
    public Category save(Category category) {
        return categoryPersistencePort.save(category);
    }

    @Override
    public Category update(Long id, Category category) {
        return categoryPersistencePort.findById(id)
                .map(categoryUpdate->{
                    categoryUpdate.setName(category.getName());
                    return categoryPersistencePort.save(categoryUpdate);
                })
                .orElseThrow(CategoryNotFoundException::new);
    }

    @Override
    public void delete(Long id) {
        if(categoryPersistencePort.findById(id).isEmpty()){
            throw new CategoryNotFoundException();
        }
        categoryPersistencePort.delete(id);
    }


}
