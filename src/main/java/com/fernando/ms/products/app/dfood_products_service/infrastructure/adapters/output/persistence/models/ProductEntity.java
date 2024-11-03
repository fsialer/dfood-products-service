package com.fernando.ms.products.app.dfood_products_service.infrastructure.adapters.output.persistence.models;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "products")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer quantity;
    private Boolean available;
    private LocalDate createAt;
    private LocalDate updateAt;
    @ManyToOne
    //@Column(name = "category_id")
    private CategoryEntity category;

}
