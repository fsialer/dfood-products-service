package com.fernando.ms.products.app.dfood_products_service.infrastructure.adapters.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fernando.ms.products.app.dfood_products_service.application.ports.input.CategoryInputPort;
import com.fernando.ms.products.app.dfood_products_service.domain.model.Category;
import com.fernando.ms.products.app.dfood_products_service.infrastructure.adapters.input.rest.mapper.CategoryRestMapper;
import com.fernando.ms.products.app.dfood_products_service.infrastructure.adapters.input.rest.models.request.CreateCategoryRequest;
import com.fernando.ms.products.app.dfood_products_service.infrastructure.adapters.input.rest.models.response.CategoryResponse;
import com.fernando.ms.products.app.dfood_products_service.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryRestAdapter.class)
public class CategoryRestAdapterTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryInputPort categoryInputPort;

    @MockBean
    private CategoryRestMapper categoryRestMapper;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        objectMapper=new ObjectMapper();
    }

    @Test
    void ShouldReturnCategoriesWhenIsRequiredAndResponseWithCodeResponse200() throws Exception {
        Category category= TestUtils.buildCategoryMock();
        CategoryResponse categoryResponse=TestUtils.buildCategoryResponseMock();
        when(categoryInputPort.findAll())
                .thenReturn(Collections.singletonList(category));
        when(categoryRestMapper.toCategoriesResponse(anyList()))
                .thenReturn(Collections.singletonList(categoryResponse));

        mockMvc.perform(get("/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.length()").value(1));

        Mockito.verify(categoryInputPort,times(1)).findAll();
        Mockito.verify(categoryRestMapper,times(1)).toCategoriesResponse(Collections.singletonList(category));
    }

    @Test
    void ShouldReturnNoneCategoriesWhenIsRequiredAndResponseWithCodeResponse200() throws Exception {
        when(categoryInputPort.findAll())
                .thenReturn(Collections.emptyList());
        when(categoryRestMapper.toCategoriesResponse(anyList()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty())
                .andExpect(jsonPath("$.length()").value(0));

        Mockito.verify(categoryInputPort,times(1)).findAll();
        Mockito.verify(categoryRestMapper,times(1)).toCategoriesResponse(Collections.emptyList());
    }

    @Test
    void shouldReturnCategoryWhenIntoIdValidAndResponseWithCodeResponse200() throws Exception {
        CategoryResponse productResponse=TestUtils.buildCategoryResponseMock();
        Category category=TestUtils.buildCategoryMock();

        when(categoryInputPort.findById(anyLong()))
                .thenReturn(category);
        when(categoryRestMapper.toCategoryResponse(any(Category.class)))
                .thenReturn(productResponse);
        mockMvc.perform(get("/categories/{id}",1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
        Mockito.verify(categoryInputPort,times(1)).findById(anyLong());
        Mockito.verify(categoryRestMapper,times(1)).toCategoryResponse(any(Category.class));
    }

    @Test
    void shouldReturnCategoryWhenIntoCategoryAndResponseWithCodeResponse201() throws Exception {
        CategoryResponse categoryResponse=TestUtils.buildCategoryResponseMock();
        Category category=TestUtils.buildCategoryMock();
        CreateCategoryRequest rq=TestUtils.buildCreateCategoryRequestMock();
        when(categoryInputPort.save(any(Category.class)))
                .thenReturn(category);
        when(categoryRestMapper.toCategoryResponse(any(Category.class)))
                .thenReturn(categoryResponse);
        when(categoryRestMapper.toCategory(any(CreateCategoryRequest.class)))
                .thenReturn(category);

        mockMvc.perform(post("/categories")
                        .content(objectMapper.writeValueAsString(rq))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
        Mockito.verify(categoryInputPort,times(1)).save(any(Category.class));
        Mockito.verify(categoryRestMapper,times(1)).toCategory(any(CreateCategoryRequest.class));
        Mockito.verify(categoryRestMapper,times(1)).toCategoryResponse(any(Category.class));

    }

    @Test
    void shouldReturnCategoryUpdatedWhenIntoCategoryByIdAndResponseWithCodeResponse200() throws Exception {
        CategoryResponse categoryResponse=TestUtils.buildCategoryResponseMock();
        Category category=TestUtils.buildCategoryMock();
        CreateCategoryRequest rq=TestUtils.buildCreateCategoryRequestMock();
        when(categoryInputPort.update(anyLong(),any(Category.class)))
                .thenReturn(category);
        when(categoryRestMapper.toCategoryResponse(any(Category.class)))
                .thenReturn(categoryResponse);
        when(categoryRestMapper.toCategory(any(CreateCategoryRequest.class)))
                .thenReturn(category);
        String productsJson = objectMapper.writeValueAsString(rq);

        mockMvc.perform(put("/categories/{id}",1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productsJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
        Mockito.verify(categoryInputPort,times(1)).update(anyLong(),any(Category.class));
        Mockito.verify(categoryRestMapper,times(1)).toCategoryResponse(any(Category.class));
        Mockito.verify(categoryRestMapper,times(1)).toCategory((any(CreateCategoryRequest.class)));
    }

    @Test
    void shouldDeleteCategoryWhenIntoIdValidAndResponseWithCodeResponse204() throws Exception {
        doNothing().when(categoryInputPort).delete(anyLong());
        mockMvc.perform(delete("/categories/{id}",1L))
                .andExpect(status().isNoContent());
        Mockito.verify(categoryInputPort,times(1)).delete(anyLong());
    }
}
