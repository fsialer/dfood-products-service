package com.fernando.ms.products.app.dfood_products_service.application.services;


import com.fernando.ms.products.app.dfood_products_service.application.ports.output.ProductPersistencePort;
import com.fernando.ms.products.app.dfood_products_service.domain.exceptions.ProductNotFoundException;
import com.fernando.ms.products.app.dfood_products_service.domain.model.Product;
import com.fernando.ms.products.app.dfood_products_service.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductPersistencePort productPersistencePort;

    @InjectMocks
    private ProductService productService;

    @Test
    void shouldReturnAllProductsWhenListHaveData(){
        when(productPersistencePort.findAll())
                .thenReturn(Collections.singletonList(TestUtils.buildProductMock()));
        List<Product> products=productService.findAll();
        assertEquals(1,products.size());
        Mockito.verify(productPersistencePort,times(1)).findAll();
    }

    @Test
    void shouldReturnNoneProductsWhenListIsRequired(){
        when(productPersistencePort.findAll())
                .thenReturn(List.of());
        List<Product> products=productService.findAll();
        assertEquals(0,products.size());
        Mockito.verify(productPersistencePort,times(1)).findAll();
    }

    @Test
    void shouldReturnOneProductWhenIntoAnIdByArgument(){
        when(productPersistencePort.findById(anyLong()))
                .thenReturn(Optional.of(TestUtils.buildProductMock()));
        Product product=productService.findById(1L);
        assertNotNull(product);

        Mockito.verify(productPersistencePort,times(1)).findById(1L);
    }

    @Test
    void shouldReturnProductNoFoundWhenIntoAnIdNotExisting(){
        when(productPersistencePort.findById(anyLong()))
                .thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class,()->productService.findById(2L));

        Mockito.verify(productPersistencePort,times(1)).findById(anyLong());
    }

    @Test
    void shouldReturnProductWhenSaveAnProductNew(){
        Product productNew=Product.builder()
                .id(1L)
                .name("Arroz con marisco")
                .description("Arroz con marisco frescos")
                .build();

        when(productPersistencePort.save(any(Product.class)))
                .thenReturn(productNew);

        Product product=productService.save(productNew);
        assertNotNull(product);
        Mockito.verify(productPersistencePort,times(1)).save(productNew);
    }

    @Test
    void shouldReturnProductWhenUpdateAnProductById(){
        Product productNew=Product.builder()
                .id(1L)
                .name("Arroz con pato")
                .description("Arroz con pato recien cocinado")
                .build();
        when(productPersistencePort.findById(anyLong()))
                .thenReturn(Optional.of(productNew));
        when(productPersistencePort.save(any(Product.class)))
                .thenReturn(productNew);

        Product productUpated=productService.update(1L,productNew);

        assertEquals(productUpated.getName(),productNew.getName());
        assertEquals(productUpated.getDescription(),productNew.getDescription());

        Mockito.verify(productPersistencePort,times(1)).findById(1L);
        Mockito.verify(productPersistencePort,times(1)).save(productNew);
    }

    @Test
    void shouldReturnProductNotFoundWhenUpdateAnProductByIdNotExisting(){
        Product productNew=Product.builder()
                .id(2L)
                .name("Seco de cabrito")
                .description("Carne tierna de cabrito")
                .build();
        when(productPersistencePort.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class,()->productService.update(2L,productNew));

        Mockito.verify(productPersistencePort,times(1)).findById(2L);
        Mockito.verify(productPersistencePort,times(0)).save(productNew);
    }

    @Test
    void shouldDeleteProductWhenAnProductByIdExist(){
        when(productPersistencePort.findById(anyLong()))
                .thenReturn(Optional.of(TestUtils.buildProductMock()));
        doNothing().when(productPersistencePort).delete(anyLong());
        productService.delete(1L);

        Mockito.verify(productPersistencePort,times(1)).findById(1L);
        Mockito.verify(productPersistencePort,times(1)).delete(1L);
    }

    @Test
    void shouldNotDeleteProductWhenAnProductByIdNoExist(){
        when(productPersistencePort.findById(anyLong()))
                .thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class,()->productService.delete(1L));
        Mockito.verify(productPersistencePort,times(1)).findById(1L);
        Mockito.verify(productPersistencePort,times(0)).delete(1L);
    }

    @Test
    void shouldFindProductsByIdsWhenIdsExists(){
        List<Long> ids = Collections.singletonList(1L);
           when(productPersistencePort.findByIds(anyCollection()))
                   .thenReturn(Collections.singletonList(TestUtils.buildProductMock()));
        List<Product> products=productService.findByIds(ids);
        assertEquals(1,products.size());
        Mockito.verify(productPersistencePort,times(1)).findByIds(anyCollection());
    }

    @Test
    void shouldFindProductsByIdsWhenIdsNotExists(){
        List<Long> ids = Collections.singletonList(2L);
        when(productPersistencePort.findByIds(anyCollection()))
                .thenReturn(Collections.singletonList(TestUtils.buildProductMock()));
        List<Product> products=productService.findByIds(ids);
        assertEquals(0,products.size());
        Mockito.verify(productPersistencePort,times(1)).findByIds(anyCollection());
    }

}
