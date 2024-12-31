package com.fernando.ms.products.app.dfood_products_service.application.services.strategy.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class InputUpdateStockProductStrategy implements IUpdateStockProductStrategy {
    @Override
    public Integer doOperation(Integer currentQuantity,Integer quantity) {
        return currentQuantity+quantity;
    }

    @Override
    public boolean isApplicable(String operation) {
        System.out.println("operation: " + operation);
        return "INPUT".equalsIgnoreCase(operation);
    }
}
