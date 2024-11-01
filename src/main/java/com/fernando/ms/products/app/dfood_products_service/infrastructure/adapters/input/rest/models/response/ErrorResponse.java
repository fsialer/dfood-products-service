package com.fernando.ms.products.app.dfood_products_service.infrastructure.adapters.input.rest.models.response;

import com.fernando.ms.products.app.dfood_products_service.infrastructure.adapters.input.rest.models.enums.ErrorType;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponse {
    private String code;
    private ErrorType type; //Functional, System
    private String message;
    private List<String> details;
    private String timestamp;
}
