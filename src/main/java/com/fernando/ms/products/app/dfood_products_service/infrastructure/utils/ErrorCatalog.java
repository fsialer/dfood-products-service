package com.fernando.ms.products.app.dfood_products_service.infrastructure.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCatalog {
    PRODUCT_NOT_FOUND("PRODUCT_MS_001", "Product not found."),
    CATEGORY_NOT_FOUND("CATEGORY_MS_001", "Category not found."),
    PRODUCTS_BAD_PARAMETERS("PRODUCTS_MS_001", "Invalid parameters for creation."),
    INTERNAL_SERVER_ERROR("PRODUCTS_MS_002", "Internal server error.");
    private final String code;
    private final String message;
}
