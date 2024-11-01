package com.fernando.ms.products.app.dfood_products_service.infrastructure.adapters.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fernando.ms.products.app.dfood_products_service.application.ports.input.ProductInputPort;
import com.fernando.ms.products.app.dfood_products_service.domain.model.Product;
import com.fernando.ms.products.app.dfood_products_service.infrastructure.adapters.input.rest.mapper.ProductRestMapper;
import com.fernando.ms.products.app.dfood_products_service.infrastructure.adapters.input.rest.models.request.CreateProductRequest;
import com.fernando.ms.products.app.dfood_products_service.infrastructure.adapters.input.rest.models.response.ProductResponse;
import com.fernando.ms.products.app.dfood_products_service.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.mockito.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

@WebMvcTest(ProductRestAdapter.class)
public class ProductRestAdapterTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductInputPort productInputPort;

    @MockBean
    private ProductRestMapper productRestMapper;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        objectMapper=new ObjectMapper();
    }

    @Test
    void ShouldReturnProductsWhenIsRequiredAndResponseWithCodeResponse200() throws Exception {
        Product product=TestUtils.buildProductMock();
        ProductResponse productResponse=TestUtils.buildProductResponseMock();
        when(productInputPort.findAll())
                .thenReturn(Collections.singletonList(product));
        when(productRestMapper.toProductsResponse(anyList()))
                .thenReturn(Collections.singletonList(productResponse));

        mockMvc.perform(get("/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.length()").value(1));

        Mockito.verify(productInputPort,times(1)).findAll();
        Mockito.verify(productRestMapper,times(1)).toProductsResponse(Collections.singletonList(product));
    }

    @Test
    void ShouldReturnNoneProductsWhenIsRequiredAndResponseWithCodeResponse200() throws Exception {
        when(productInputPort.findAll())
                .thenReturn(Collections.emptyList());
        when(productRestMapper.toProductsResponse(anyList()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty())
                .andExpect(jsonPath("$.length()").value(0));

        Mockito.verify(productInputPort,times(1)).findAll();
        Mockito.verify(productRestMapper,times(1)).toProductsResponse(Collections.emptyList());
    }

    @Test
    void shouldReturnProductWhenIntoIdValidAndResponseWithCodeResponse200() throws Exception {
        ProductResponse productResponse=TestUtils.buildProductResponseMock();
        Product product=TestUtils.buildProductMock();

        when(productInputPort.findById(anyLong()))
                .thenReturn(product);
        when(productRestMapper.toProductResponse(any(Product.class)))
                .thenReturn(productResponse);
        mockMvc.perform(get("/products/{id}",1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
        Mockito.verify(productInputPort,times(1)).findById(1L);
        Mockito.verify(productRestMapper,times(1)).toProductResponse(product);
    }

    @Test
    void shouldReturnProductWhenIntoProductAndResponseWithCodeResponse201() throws Exception {
        ProductResponse productResponse=TestUtils.buildProductResponseMock();
        Product product=TestUtils.buildProductMock();
        CreateProductRequest rq=TestUtils.buildCreateProductRequestMock();
        when(productInputPort.save(any(Product.class)))
                .thenReturn(product);
        when(productRestMapper.toProductResponse(any(Product.class)))
                .thenReturn(productResponse);
        when(productRestMapper.toProduct(any(CreateProductRequest.class)))
                .thenReturn(product);
        String productsJson = objectMapper.writeValueAsString(rq);


        mockMvc.perform(post("/products")
                        .content(productsJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
        Mockito.verify(productInputPort,times(1)).save((any(Product.class)));
        Mockito.verify(productRestMapper,times(1)).toProduct(any(CreateProductRequest.class));
        Mockito.verify(productRestMapper,times(1)).toProductResponse(any(Product.class));

    }

    @Test
    void shouldReturnProductUpdatedWhenIntoProductByIdAndResponseWithCodeResponse200() throws Exception {
        ProductResponse productResponse=TestUtils.buildProductResponseMock();
        Product product=TestUtils.buildProductMock();
        CreateProductRequest rq=TestUtils.buildCreateProductRequestMock();
        when(productInputPort.update(anyLong(),any(Product.class)))
                .thenReturn(product);
        when(productRestMapper.toProductResponse(any(Product.class)))
                .thenReturn(productResponse);
        when(productRestMapper.toProduct(any(CreateProductRequest.class)))
                .thenReturn(product);
        String productsJson = objectMapper.writeValueAsString(rq);

        mockMvc.perform(put("/products/{id}",1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(productsJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
        Mockito.verify(productInputPort,times(1)).update(anyLong(),any(Product.class));
        Mockito.verify(productRestMapper,times(1)).toProductResponse(any(Product.class));
        Mockito.verify(productRestMapper,times(1)).toProduct(any(CreateProductRequest.class));
    }

    @Test
    void shouldDeleteProductWhenIntoIdValidAndResponseWithCodeResponse204() throws Exception {
        doNothing().when(productInputPort).delete(anyLong());
        mockMvc.perform(delete("/products/{id}",1L))
                .andExpect(status().isNoContent());
        Mockito.verify(productInputPort,times(1)).delete(anyLong());
    }


}
