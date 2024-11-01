package com.fernando.ms.products.app.dfood_products_service.infrastructure.adapters.output.persistence.repository;

import com.fernando.ms.products.app.dfood_products_service.infrastructure.adapters.output.persistence.models.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryJpaRepository extends JpaRepository<CategoryEntity,Long> {
}
