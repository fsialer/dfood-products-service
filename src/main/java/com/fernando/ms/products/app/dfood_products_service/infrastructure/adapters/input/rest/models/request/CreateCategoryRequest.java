package com.fernando.ms.products.app.dfood_products_service.infrastructure.adapters.input.rest.models.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCategoryRequest {
    @NotBlank(message = "Field name cannot be null or blank")
    private String name;
}
