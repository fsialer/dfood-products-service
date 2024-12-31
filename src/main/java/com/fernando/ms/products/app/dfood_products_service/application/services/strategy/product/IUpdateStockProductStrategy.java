package com.fernando.ms.products.app.dfood_products_service.application.services.strategy.product;

public interface IUpdateStockProductStrategy {
    Integer doOperation(Integer currentQuantity,Integer quantity);
    boolean isApplicable(String operation);
}
