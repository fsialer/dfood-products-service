package com.fernando.ms.products.app.dfood_products_service.infrastructure.adapters.output.persistence.repository;

import com.fernando.ms.products.app.dfood_products_service.infrastructure.adapters.output.persistence.models.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductJpaRepository extends JpaRepository<ProductEntity,Long> {
}
