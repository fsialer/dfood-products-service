package com.fernando.ms.products.app.dfood_products_service.infrastructure.adapters.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fernando.ms.products.app.dfood_products_service.application.ports.input.CategoryInputPort;
import com.fernando.ms.products.app.dfood_products_service.application.ports.input.ProductInputPort;
import com.fernando.ms.products.app.dfood_products_service.domain.exceptions.CategoryNotFoundException;
import com.fernando.ms.products.app.dfood_products_service.domain.exceptions.OutputStockRuleException;
import com.fernando.ms.products.app.dfood_products_service.domain.exceptions.ProductNotFoundException;
import com.fernando.ms.products.app.dfood_products_service.domain.exceptions.UpdateStockStrategyNotFoundException;
import com.fernando.ms.products.app.dfood_products_service.infrastructure.adapters.input.rest.mapper.CategoryRestMapper;
import com.fernando.ms.products.app.dfood_products_service.infrastructure.adapters.input.rest.mapper.ProductRestMapper;
import com.fernando.ms.products.app.dfood_products_service.infrastructure.adapters.input.rest.models.response.ErrorResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.fernando.ms.products.app.dfood_products_service.infrastructure.adapters.input.rest.models.enums.ErrorType.FUNCTIONAL;
import static com.fernando.ms.products.app.dfood_products_service.infrastructure.adapters.input.rest.models.enums.ErrorType.SYSTEM;
import static com.fernando.ms.products.app.dfood_products_service.infrastructure.utils.ErrorCatalog.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {CategoryRestAdapter.class,ProductRestAdapter.class})
public class GlobalControllerAdviceTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryInputPort categoryInputPort;

    @MockBean
    private CategoryRestMapper categoryRestMapper;

    @MockBean
    private ProductInputPort productInputPort;

    @MockBean
    private ProductRestMapper productRestMapper;

    @Test
    void whenThrowsProductNotFoundExceptionThenReturnNotFound() throws Exception {
        when(productInputPort.findById(anyLong()))
                .thenThrow(new ProductNotFoundException());
        mockMvc.perform(get("/products/{id}",1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result->{
                    ErrorResponse errorResponse=objectMapper.readValue(
                            result.getResponse().getContentAsString(), ErrorResponse.class);
                    assertAll(
                            ()->assertEquals(PRODUCT_NOT_FOUND.getCode(),errorResponse.getCode()),
                            ()->assertEquals(FUNCTIONAL,errorResponse.getType()),
                            ()->assertEquals(PRODUCT_NOT_FOUND.getMessage(),errorResponse.getMessage()),
                            ()->assertNotNull(errorResponse.getTimestamp())
                    );
                });

    }

    @Test
    void whenThrowsCategoryNotFoundExceptionThenReturnNotFound() throws Exception {
        when(categoryInputPort.findById(anyLong()))
                .thenThrow(new CategoryNotFoundException());
        mockMvc.perform(get("/categories/{id}",1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result->{
                    ErrorResponse errorResponse=objectMapper.readValue(
                            result.getResponse().getContentAsString(), ErrorResponse.class);
                    assertAll(
                            ()->assertEquals(CATEGORY_NOT_FOUND.getCode(),errorResponse.getCode()),
                            ()->assertEquals(FUNCTIONAL,errorResponse.getType()),
                            ()->assertEquals(CATEGORY_NOT_FOUND.getMessage(),errorResponse.getMessage()),
                            ()->assertNotNull(errorResponse.getTimestamp())
                    );
                });

    }

    @Test
    void whenThrowsMethodArgumentNotValidExceptionOfCategoryThenReturnBadRequest() throws Exception {
        mockMvc.perform(post("/categories")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(result->{
                    ErrorResponse errorResponse=objectMapper.readValue(
                            result.getResponse().getContentAsString(), ErrorResponse.class
                    );

                    assertAll(
                            ()->assertEquals(PRODUCTS_BAD_PARAMETERS.getCode(),errorResponse.getCode()),
                            ()->assertEquals(FUNCTIONAL,errorResponse.getType()),
                            ()->assertEquals(PRODUCTS_BAD_PARAMETERS.getMessage(),errorResponse.getMessage()),
                            ()->assertNotNull(errorResponse.getDetails()),
                            ()->assertNotNull(errorResponse.getTimestamp())
                    );
                });
    }

    @Test
    void whenThrowsMethodArgumentNotValidExceptionOfProductThenReturnBadRequest() throws Exception {
        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(result->{
                    ErrorResponse errorResponse=objectMapper.readValue(
                            result.getResponse().getContentAsString(), ErrorResponse.class
                    );

                    assertAll(
                            ()->assertEquals(PRODUCTS_BAD_PARAMETERS.getCode(),errorResponse.getCode()),
                            ()->assertEquals(FUNCTIONAL,errorResponse.getType()),
                            ()->assertEquals(PRODUCTS_BAD_PARAMETERS.getMessage(),errorResponse.getMessage()),
                            ()->assertNotNull(errorResponse.getDetails()),
                            ()->assertNotNull(errorResponse.getTimestamp())
                    );
                });
    }

    @Test
    @DisplayName("Expect OrderStrategyException When Operation Stock Products is Invalid")
    void Expect_OrderStrategyException_When_OperationStockProductsIsInvalid() throws Exception {
        when(productInputPort.updateStock(anyLong(),anyInt(),anyString()))
                .thenThrow(new UpdateStockStrategyNotFoundException("Operation not valid: ANY"));
        mockMvc.perform(put("/products/{id}/update-stock/{quantity}/operation/{operation}}",1L,11,"ANY")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result->{
                    ErrorResponse errorResponse=objectMapper.readValue(
                            result.getResponse().getContentAsString(), ErrorResponse.class);
                    assertAll(
                            ()->assertEquals(OPERATION_UPDATE_STOCK_NOT_FOUND.getCode(),errorResponse.getCode()),
                            ()->assertEquals(FUNCTIONAL,errorResponse.getType()),
                            ()->assertEquals(OPERATION_UPDATE_STOCK_NOT_FOUND.getMessage(),errorResponse.getMessage()),
                            ()->assertNotNull(errorResponse.getTimestamp())
                    );
                });
    }

    @Test
    @DisplayName("Expect OrderStrategyException When Operation Stock Products is Invalid")
    void Expect_OutputStockRuleException_When_Product() throws Exception {
        when(productInputPort.updateStock(anyLong(),anyInt(),anyString()))
                .thenThrow(new OutputStockRuleException("Current stock is zero"));
        mockMvc.perform(put("/products/{id}/update-stock/{quantity}/operation/{operation}}",1L,11,"OUTPUT")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result->{
                    ErrorResponse errorResponse=objectMapper.readValue(
                            result.getResponse().getContentAsString(), ErrorResponse.class);
                    assertAll(
                            ()->assertEquals(OUTPUT_STOCK_RULE.getCode(),errorResponse.getCode()),
                            ()->assertEquals(FUNCTIONAL,errorResponse.getType()),
                            ()->assertEquals(OUTPUT_STOCK_RULE.getMessage(),errorResponse.getMessage()),
                            ()->assertNotNull(errorResponse.getTimestamp())
                    );
                });
    }

    @Test
    void whenThrowsGenericExceptionThenReturnInternalServerErrorResponse() throws Exception {
        when(categoryInputPort.findAll())
                .thenThrow(new RuntimeException("Generic error"));
        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(result->{
                    ErrorResponse errorResponse=objectMapper.readValue(
                            result.getResponse().getContentAsString(), ErrorResponse.class
                    );

                    assertAll(
                            ()->assertEquals(INTERNAL_SERVER_ERROR.getCode(),errorResponse.getCode()),
                            ()->assertEquals(SYSTEM,errorResponse.getType()),
                            ()->assertEquals(INTERNAL_SERVER_ERROR.getMessage(),errorResponse.getMessage()),
                            ()->assertNotNull(errorResponse.getDetails()),
                            ()->assertNotNull(errorResponse.getTimestamp())
                    );
                });
    }

}
