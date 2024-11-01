package com.fernando.ms.products.app.dfood_products_service.infrastructure.adapters.input.rest.mapper;

import com.fernando.ms.products.app.dfood_products_service.domain.model.Product;
import com.fernando.ms.products.app.dfood_products_service.infrastructure.adapters.input.rest.models.request.CreateProductRequest;
import com.fernando.ms.products.app.dfood_products_service.infrastructure.adapters.input.rest.models.response.ProductResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductRestMapper {

    ProductResponse toProductResponse(Product product);
    Product toProduct(CreateProductRequest rq);
    List<ProductResponse> toProductsResponse(List<Product> products);
}
