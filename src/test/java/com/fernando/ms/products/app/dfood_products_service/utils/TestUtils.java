package com.fernando.ms.products.app.dfood_products_service.utils;

import com.fernando.ms.products.app.dfood_products_service.domain.model.Category;
import com.fernando.ms.products.app.dfood_products_service.domain.model.Product;
import com.fernando.ms.products.app.dfood_products_service.infrastructure.adapters.input.rest.models.request.CreateCategoryRequest;
import com.fernando.ms.products.app.dfood_products_service.infrastructure.adapters.input.rest.models.request.CreateProductRequest;
import com.fernando.ms.products.app.dfood_products_service.infrastructure.adapters.input.rest.models.response.CategoryResponse;
import com.fernando.ms.products.app.dfood_products_service.infrastructure.adapters.input.rest.models.response.ProductResponse;
import com.fernando.ms.products.app.dfood_products_service.infrastructure.adapters.output.persistence.models.CategoryEntity;
import com.fernando.ms.products.app.dfood_products_service.infrastructure.adapters.output.persistence.models.ProductEntity;

import java.time.LocalDate;

public class TestUtils {

    public static Product buildProductMock(){
        return Product.builder()
                .id(1L)
                .name("Arroz con mariscos")
                .description("Arroz con los mariscos frescos del dia")
                .price(15.0)
                .quantity(14)
                .available(true)
                .category(Category
                        .builder()
                        .id(1L)
                        .name("comida")
                        .build())
                .build();
    }

    public static ProductEntity buildProductEntityMock(){
        return ProductEntity.builder()
                .id(1L)
                .name("Arroz con mariscos")
                .description("Arroz con los mariscos frescos del dia")
                .price(15.0)
                .quantity(14)
                .available(true)
                .category(CategoryEntity
                        .builder()
                        .id(1L)
                        .name("comida")
                        .build())
                .createAt(LocalDate.now())
                .updateAt(LocalDate.now())
                .build();
    }

    public static ProductResponse buildProductResponseMock(){
        return ProductResponse.builder()
                .id(1L)
                .name("Arroz con mariscos")
                .description("Arroz con los mariscos frescos del dia")
                .price(15.0)
                .quantity(14)
                .available(true)
                .category(CategoryResponse
                        .builder()
                        .name("comida")
                        .build())
                .build();
    }

    public static CreateProductRequest buildCreateProductRequestMock(){
        return CreateProductRequest.builder()
                .name("Arroz con mariscos")
                .description("Arroz con los mariscos frescos del dia")
                .price(15.0)
                .quantity(14)
                .available(true)
                .category(CreateCategoryRequest
                        .builder()
                        .name("comida")
                        .build())
                .build();
    }

    public static Category buildCategoryMock(){
        return Category.builder()
                .id(1L)
                .name("Comida")
                .build();
    }

    public static CategoryEntity buildCategoryEntityMock(){
        return CategoryEntity.builder()
                .id(1L)
                .name("Comida")
                .build();
    }

    public static CreateCategoryRequest buildCreateCategoryRequestMock(){
        return CreateCategoryRequest.builder()
                .name("Comida")
                .build();
    }

    public static CategoryResponse buildCategoryResponseMock(){
        return CategoryResponse.builder()
                .id(1L)
                .name("Comida")
                .build();
    }
}
