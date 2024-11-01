package com.fernando.ms.products.app.dfood_products_service.infrastructure.adapters.input.rest.mapper;


import com.fernando.ms.products.app.dfood_products_service.domain.model.Category;
import com.fernando.ms.products.app.dfood_products_service.infrastructure.adapters.input.rest.models.request.CreateCategoryRequest;
import com.fernando.ms.products.app.dfood_products_service.infrastructure.adapters.input.rest.models.response.CategoryResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryRestMapper {
    CategoryResponse toCategoryResponse(Category category);
    Category toCategory(CreateCategoryRequest rq);
    List<CategoryResponse> toCategoriesResponse(List<Category> categories);
}
