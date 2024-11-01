package com.fernando.ms.products.app.dfood_products_service.infrastructure.adapters.input.rest.models.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer quantity;
    private Boolean available;
    private CategoryResponse category;
}
