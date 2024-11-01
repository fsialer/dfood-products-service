package com.fernando.ms.products.app.dfood_products_service.domain.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer quantity;
    private Boolean available;
    private Category category;
}
