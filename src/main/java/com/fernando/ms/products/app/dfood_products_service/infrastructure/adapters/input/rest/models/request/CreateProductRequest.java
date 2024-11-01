package com.fernando.ms.products.app.dfood_products_service.infrastructure.adapters.input.rest.models.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateProductRequest {
    @NotBlank(message = "Field name cannot be null or blank")
    private String name;
    @NotBlank(message = "Field description cannot be null or blank")
    private String description;
    @NotNull(message = "Field price cannot be null")
    private Double price;
    @NotNull(message = "Field quantity cannot be null")
    private Integer quantity;
    @NotNull(message = "Field available cannot be null or blank")
    private Boolean available;
    private CreateCategoryRequest category;
}
