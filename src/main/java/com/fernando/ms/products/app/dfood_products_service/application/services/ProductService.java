package com.fernando.ms.products.app.dfood_products_service.application.services;

import com.fernando.ms.products.app.dfood_products_service.application.ports.input.ProductInputPort;
import com.fernando.ms.products.app.dfood_products_service.application.ports.output.ProductPersistencePort;
import com.fernando.ms.products.app.dfood_products_service.domain.exceptions.ProductNotFoundException;
import com.fernando.ms.products.app.dfood_products_service.domain.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService implements ProductInputPort {

    private final ProductPersistencePort persistencePort;

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
        return persistencePort.save(product);
    }

    @Override
    public Product update(Long id, Product product) {
        return persistencePort
                .findById(id)
                .map(productUpdated->{
                    productUpdated.setName(product.getName());
                    productUpdated.setDescription(product.getDescription());
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
