package com.fernando.ms.products.app.dfood_products_service.application.services.strategy.product;

import com.fernando.ms.products.app.dfood_products_service.domain.exceptions.OutputStockRuleException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OutputUpdateStockProductStrategy implements IUpdateStockProductStrategy {
    @Override
    public Integer doOperation(Integer currentQuantity,Integer quantity) {
        if(currentQuantity==0){
            throw new OutputStockRuleException("Current stock is zero");
        }

        if(quantity>currentQuantity){
            throw new OutputStockRuleException("Quantity to output is greater than current stock");
        }
        return currentQuantity-quantity;
    }

    @Override
    public boolean isApplicable(String operation) {
        return "OUTPUT".equalsIgnoreCase(operation);
    }
}
