package com.fernando.ms.products.app.dfood_products_service.application.services;

import com.fernando.ms.products.app.dfood_products_service.application.ports.input.ProductInputPort;
import com.fernando.ms.products.app.dfood_products_service.application.ports.output.CategoryPersistencePort;
import com.fernando.ms.products.app.dfood_products_service.application.ports.output.ProductPersistencePort;
import com.fernando.ms.products.app.dfood_products_service.domain.exceptions.CategoryNotFoundException;
import com.fernando.ms.products.app.dfood_products_service.domain.exceptions.ProductNotFoundException;
import com.fernando.ms.products.app.dfood_products_service.domain.model.Category;
import com.fernando.ms.products.app.dfood_products_service.domain.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProductService implements ProductInputPort {

    private final ProductPersistencePort persistencePort;

    private final CategoryPersistencePort categoryPersistencePort;

    @Override
    public List<Product> findAll() {
        return persistencePort.findAll();
    }

    @Override
    public Product findById(Long id) {
        return persistencePort.findById(id).orElseThrow(ProductNotFoundException::new);
    }

    @Override
    public Product save(Product product) {
        Category category=categoryPersistencePort.findById(product.getCategory().getId()).orElseThrow(CategoryNotFoundException::new);
        product.setCategory(category);
        return persistencePort.save(product);
    }

    @Override
    public Product update(Long id, Product product) {
        return persistencePort
                .findById(id)
                .map(productUpdated->{
                    productUpdated.setName(product.getName());
                    productUpdated.setDescription(product.getDescription());
                    productUpdated.setPrice(product.getPrice());
                    productUpdated.setQuantity(product.getQuantity());
                    productUpdated.setAvailable(product.getAvailable());
                    if(!Objects.equals(productUpdated.getCategory().getId(), product.getCategory().getId())){
                        Category category=categoryPersistencePort.findById(product.getCategory().getId()).orElseThrow(CategoryNotFoundException::new);
                        productUpdated.setCategory(category);
                    }
                    persistencePort.save(productUpdated);
                    return productUpdated;
                }).orElseThrow(ProductNotFoundException::new);
    }

    @Override
    public void delete(Long id) {
        if(persistencePort.findById(id).isEmpty()){
            throw  new ProductNotFoundException();
        }
        persistencePort.delete(id);
    }

    @Override
    public List<Product> findByIds(Iterable<Long> ids) {
        return persistencePort.findByIds(ids);
    }
}
