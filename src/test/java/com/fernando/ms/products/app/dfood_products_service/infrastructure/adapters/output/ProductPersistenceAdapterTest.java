package com.fernando.ms.products.app.dfood_products_service.infrastructure.adapters.output;

import com.fernando.ms.products.app.dfood_products_service.domain.model.Product;
import com.fernando.ms.products.app.dfood_products_service.infrastructure.adapters.output.persistence.mapper.ProductPersistenceMapper;
import com.fernando.ms.products.app.dfood_products_service.infrastructure.adapters.output.persistence.models.ProductEntity;
import com.fernando.ms.products.app.dfood_products_service.infrastructure.adapters.output.persistence.ProductPersistenceAdapter;
import com.fernando.ms.products.app.dfood_products_service.infrastructure.adapters.output.persistence.repository.ProductJpaRepository;
import com.fernando.ms.products.app.dfood_products_service.utils.TestUtils;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductPersistenceAdapterTest {
    @Mock
    private ProductJpaRepository repository;

    @Mock
    private ProductPersistenceMapper mapper;

    @InjectMocks
    private ProductPersistenceAdapter persistenceAdapter;

    @Test
    void shouldConsultProductsWhenIsRequired(){
        when(repository.findAll())
                .thenReturn(Collections.singletonList(TestUtils.buildProductEntityMock()));
        when(mapper.toProducts(anyList()))
                .thenReturn(Collections.singletonList(TestUtils.buildProductMock()));
        List<Product> products=persistenceAdapter.findAll();
        assertEquals(1,products.size());
        Mockito.verify(mapper,times(1)).toProducts(anyList());
        Mockito.verify(repository,times(1)).findAll();
    }

    @Test
    void shouldReturnVoidProductsWhenIsRequired(){
        when(repository.findAll())
                .thenReturn(Collections.emptyList());
        when(mapper.toProducts(anyList()))
                .thenReturn(Collections.emptyList());
        List<Product> products=persistenceAdapter.findAll();
        assertEquals(0,products.size());
        Mockito.verify(mapper,times(1)).toProducts(anyList());
        Mockito.verify(repository,times(1)).findAll();
    }

    @Test
    void shouldConsultProductWhenIntoIdByArgument(){
        when(repository.findById(anyLong()))
                .thenReturn(Optional.of(TestUtils.buildProductEntityMock()));
        when(mapper.toProduct(any(ProductEntity.class)))
                .thenReturn(TestUtils.buildProductMock());
        Optional<Product> product=persistenceAdapter.findById(1L);
        ArgumentCaptor<ProductEntity> captor = ArgumentCaptor.forClass(ProductEntity.class);
        assertThat(product.isPresent()).isEqualTo(true);
        Mockito.verify(mapper,times(1)).toProduct(captor.capture());
        Mockito.verify(repository,times(1)).findById(1L);
    }

    @Test
    void shouldReturnNullProductWhenIntoIdByArgumentNotExisted(){
        ProductEntity productEntity=TestUtils.buildProductEntityMock();
        when(repository.findById(anyLong()))
                .thenReturn(Optional.empty());
        Optional<Product> product=persistenceAdapter.findById(2L);
        assertTrue(product.isEmpty());
        Mockito.verify(repository,times(1)).findById(2L);
        Mockito.verify(mapper,times(0)).toProduct(productEntity);
    }

    @Test
    void shouldRegisterProductWhenIntoProduct(){
        ProductEntity productEntity=TestUtils.buildProductEntityMock();
        Product product=TestUtils.buildProductMock();
        when(repository.save(any(ProductEntity.class)))
                .thenReturn(productEntity);
        when(mapper.toProduct(any(ProductEntity.class)))
                .thenReturn(product);
        when(mapper.toProductEntity(any(Product.class)))
                .thenReturn(productEntity);
        Product productNew=persistenceAdapter.save(product);

        assertEquals(productNew,product);
        Mockito.verify(mapper,times(1)).toProduct(productEntity);
        Mockito.verify(mapper,times(1)).toProductEntity(product);
        Mockito.verify(repository,times(1)).save(productEntity);
    }

    @Test
    void shouldDeleteProductWhenIntoAnIdValid(){
        doNothing().when(repository).deleteById(anyLong());
        persistenceAdapter.delete(1L);
        Mockito.verify(repository,times(1)).deleteById(1L);
    }

    @Test
    void shouldConsultProductsWhenIdsExisting(){
        List<Long> ids = Collections.singletonList(1L);
        when(repository.findAllById(anyCollection()))
                .thenReturn(Collections.singletonList(TestUtils.buildProductEntityMock()));
        when(mapper.toProducts(anyList()))
                .thenReturn(Collections.singletonList(TestUtils.buildProductMock()));
        List<Product> products=persistenceAdapter.findByIds(ids);
        assertEquals(1,products.size());
        Mockito.verify(mapper,times(1)).toProducts(anyList());
        Mockito.verify(repository,times(1)).findAllById(anyCollection());
    }

    @Test
    void shouldConsultProductsWhenIdsNotExisting(){
        List<Long> ids = Collections.singletonList(2L);
        when(repository.findAllById(anyCollection()))
                .thenReturn(Collections.emptyList());
        when(mapper.toProducts(anyList()))
                .thenReturn(Collections.emptyList());
        List<Product> products=persistenceAdapter.findByIds(ids);
        assertEquals(0,products.size());
        Mockito.verify(mapper,times(1)).toProducts(anyList());
        Mockito.verify(repository,times(1)).findAllById(anyCollection());
    }
}
