package com.fernando.ms.products.app.dfood_products_service.infrastructure.adapters.output.persistence.mapper;

import com.fernando.ms.products.app.dfood_products_service.domain.model.Category;
import com.fernando.ms.products.app.dfood_products_service.infrastructure.adapters.output.persistence.models.CategoryEntity;
import org.mapstruct.Mapper;

import java.util.List;


@Mapper(componentModel = "spring")
public interface CategoryPersistenceMapper {
    CategoryEntity toCategoryEntity(Category category);
    Category toCategory(CategoryEntity category);
    List<Category> toCategories(List<CategoryEntity> categories);
}
