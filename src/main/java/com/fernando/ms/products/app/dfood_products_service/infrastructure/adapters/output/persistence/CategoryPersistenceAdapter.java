package com.fernando.ms.products.app.dfood_products_service.infrastructure.adapters.output.persistence;

import com.fernando.ms.products.app.dfood_products_service.application.ports.output.CategoryPersistencePort;
import com.fernando.ms.products.app.dfood_products_service.domain.model.Category;
import com.fernando.ms.products.app.dfood_products_service.infrastructure.adapters.output.persistence.mapper.CategoryPersistenceMapper;
import com.fernando.ms.products.app.dfood_products_service.infrastructure.adapters.output.persistence.repository.CategoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CategoryPersistenceAdapter implements CategoryPersistencePort {
    private final CategoryJpaRepository jpaRepository;
    private final CategoryPersistenceMapper mapper;

    @Override
    public List<Category> findAll() {
        return mapper.toCategories(jpaRepository.findAll());
    }

    @Override
    public Optional<Category> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toCategory);
    }

    @Override
    public Category save(Category category) {
        return mapper.toCategory(jpaRepository.save(mapper.toCategoryEntity(category)));
    }

    @Override
    public void delete(Long id) {
        jpaRepository.deleteById(id);
    }
}
