package com.fernando.ms.products.app.dfood_products_service.infrastructure.adapters.output.persistence.mapper;

import com.fernando.ms.products.app.dfood_products_service.domain.model.Product;
import com.fernando.ms.products.app.dfood_products_service.infrastructure.adapters.output.persistence.models.ProductEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductPersistenceMapper {

    Product toProduct(ProductEntity product);
    ProductEntity toProductEntity(Product product);
    List<Product> toProducts(List<ProductEntity> products);
}
