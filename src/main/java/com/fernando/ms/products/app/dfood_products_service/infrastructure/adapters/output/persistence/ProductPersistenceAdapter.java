package com.fernando.ms.products.app.dfood_products_service.infrastructure.adapters.output.persistence;

import com.fernando.ms.products.app.dfood_products_service.application.ports.output.ProductPersistencePort;
import com.fernando.ms.products.app.dfood_products_service.domain.model.Product;
import com.fernando.ms.products.app.dfood_products_service.infrastructure.adapters.output.persistence.mapper.ProductPersistenceMapper;
import com.fernando.ms.products.app.dfood_products_service.infrastructure.adapters.output.persistence.repository.ProductJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductPersistenceAdapter implements ProductPersistencePort {
    private final ProductJpaRepository repository;
    private final ProductPersistenceMapper mapper;

    @Override
    public List<Product> findAll() {
        return mapper.toProducts(repository.findAll());
    }

    @Override
    public Optional<Product> findById(Long id) {
        return repository.findById(id).map(mapper::toProduct);
    }

    @Override
    public Product save(Product product) {
        return mapper.toProduct(repository.save(mapper.toProductEntity(product)));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<Product> findByIds(Iterable<Long> ids) {
        return mapper.toProducts(repository.findAllById(ids));
    }
}
