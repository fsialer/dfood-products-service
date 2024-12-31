package com.fernando.ms.products.app.dfood_products_service.infrastructure.adapters.input.rest;

import com.fernando.ms.products.app.dfood_products_service.domain.exceptions.CategoryNotFoundException;
import com.fernando.ms.products.app.dfood_products_service.domain.exceptions.ProductNotFoundException;

import com.fernando.ms.products.app.dfood_products_service.domain.exceptions.OutputStockRuleException;
import com.fernando.ms.products.app.dfood_products_service.domain.exceptions.UpdateStockStrategyNotFoundException;
import com.fernando.ms.products.app.dfood_products_service.infrastructure.adapters.input.rest.models.response.ErrorResponse;

import static com.fernando.ms.products.app.dfood_products_service.infrastructure.adapters.input.rest.models.enums.ErrorType.SYSTEM;
import static com.fernando.ms.products.app.dfood_products_service.infrastructure.adapters.input.rest.models.enums.ErrorType.FUNCTIONAL;
import static com.fernando.ms.products.app.dfood_products_service.infrastructure.utils.ErrorCatalog.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDate;
import java.util.Collections;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ProductNotFoundException.class)
    public ErrorResponse handleProductNotFoundException() {
        return ErrorResponse.builder()
                .code(PRODUCT_NOT_FOUND.getCode())
                .type(FUNCTIONAL)
                .message(PRODUCT_NOT_FOUND.getMessage())
                .timestamp(LocalDate.now().toString())
                .build();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CategoryNotFoundException.class)
    public ErrorResponse handleCategoryNotFoundException() {
        return ErrorResponse.builder()
                .code(CATEGORY_NOT_FOUND.getCode())
                .type(FUNCTIONAL)
                .message(CATEGORY_NOT_FOUND.getMessage())
                .timestamp(LocalDate.now().toString())
                .build();
    }



    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        return ErrorResponse.builder()
                .code(PRODUCTS_BAD_PARAMETERS.getCode())
                .type(FUNCTIONAL)
                .message(PRODUCTS_BAD_PARAMETERS.getMessage())
                .details(bindingResult.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage())
                        .toList())
                .timestamp(LocalDate.now().toString())
                .build();
    }

    @ExceptionHandler(UpdateStockStrategyNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleUpdateStockStrategyNotFoundException(UpdateStockStrategyNotFoundException e){
        return ErrorResponse.builder()
                .code(OPERATION_UPDATE_STOCK_NOT_FOUND.getCode())
                .type(FUNCTIONAL)
                .message(OPERATION_UPDATE_STOCK_NOT_FOUND.getMessage())
                .details(Collections.singletonList(e.getMessage()))
                .timestamp(LocalDate.now().toString())
                .build();
    }

    @ExceptionHandler(OutputStockRuleException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleStockZeroException(OutputStockRuleException e){
        return ErrorResponse.builder()
                .code(OUTPUT_STOCK_RULE.getCode())
                .type(FUNCTIONAL)
                .message(OUTPUT_STOCK_RULE.getMessage())
                .details(Collections.singletonList(e.getMessage()))
                .timestamp(LocalDate.now().toString())
                .build();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleException(Exception e) {
        return ErrorResponse.builder()
                .code(INTERNAL_SERVER_ERROR.getCode())
                .type(SYSTEM)
                .message(INTERNAL_SERVER_ERROR.getMessage())
                .details(Collections.singletonList(e.getMessage()))
                .timestamp(LocalDate.now().toString())
                .build();
    }
}