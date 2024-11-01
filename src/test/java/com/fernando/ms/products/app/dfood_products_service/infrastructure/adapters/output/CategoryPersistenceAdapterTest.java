package com.fernando.ms.products.app.dfood_products_service.infrastructure.adapters.output;

import com.fernando.ms.products.app.dfood_products_service.domain.model.Category;
import com.fernando.ms.products.app.dfood_products_service.infrastructure.adapters.output.persistence.mapper.CategoryPersistenceMapper;
import com.fernando.ms.products.app.dfood_products_service.infrastructure.adapters.output.persistence.models.CategoryEntity;
import com.fernando.ms.products.app.dfood_products_service.infrastructure.adapters.output.persistence.CategoryPersistenceAdapter;
import com.fernando.ms.products.app.dfood_products_service.infrastructure.adapters.output.persistence.repository.CategoryJpaRepository;
import com.fernando.ms.products.app.dfood_products_service.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class CategoryPersistenceAdapterTest {
    @Mock
    private CategoryJpaRepository repository;

    @Mock
    private CategoryPersistenceMapper mapper;

    @InjectMocks
    private CategoryPersistenceAdapter persistenceAdapter;

    @Test
    void shouldConsultCategoriesWhenIsRequired(){
        when(repository.findAll())
                .thenReturn(Collections.singletonList(TestUtils.buildCategoryEntityMock()));
        when(mapper.toCategories(anyList()))
                .thenReturn(Collections.singletonList(TestUtils.buildCategoryMock()));
        List<Category> categories=persistenceAdapter.findAll();
        assertEquals(1,categories.size());
        Mockito.verify(mapper,times(1)).toCategories(anyList());
        Mockito.verify(repository,times(1)).findAll();
    }

    @Test
    void shouldReturnVoidCategoriesWhenIsRequired(){
        when(repository.findAll())
                .thenReturn(Collections.emptyList());
        when(mapper.toCategories(anyList()))
                .thenReturn(Collections.emptyList());
        List<Category> categories=persistenceAdapter.findAll();
        assertEquals(0,categories.size());
        Mockito.verify(mapper,times(1)).toCategories(anyList());
        Mockito.verify(repository,times(1)).findAll();
    }

    @Test
    void shouldConsultCategoryWhenIntoId(){
        when(repository.findById(anyLong()))
                .thenReturn(Optional.of(TestUtils.buildCategoryEntityMock()));
        when(mapper.toCategory(any(CategoryEntity.class)))
                .thenReturn(TestUtils.buildCategoryMock());
        Optional<Category> category=persistenceAdapter.findById(1L);
        assertThat(category.isPresent()).isEqualTo(true);
        Mockito.verify(mapper,times(1)).toCategory(any(CategoryEntity.class));
        Mockito.verify(repository,times(1)).findById(anyLong());
    }

    @Test
    void shouldReturnNullCategoryWhenIntoIdByArgumentNotExisted(){
        when(repository.findById(anyLong()))
                .thenReturn(Optional.empty());
        Optional<Category> category=persistenceAdapter.findById(2L);
        assertTrue(category.isEmpty());
        Mockito.verify(repository,times(1)).findById(anyLong());
        Mockito.verify(mapper,times(0)).toCategory(any(CategoryEntity.class));
    }

    @Test
    void shouldRegisterProductWhenIntoProduct(){
        CategoryEntity categoryEntity=TestUtils.buildCategoryEntityMock();
        Category product=TestUtils.buildCategoryMock();
        when(repository.save(any(CategoryEntity.class)))
                .thenReturn(categoryEntity);
        when(mapper.toCategory(any(CategoryEntity.class)))
                .thenReturn(product);
        when(mapper.toCategoryEntity(any(Category.class)))
                .thenReturn(categoryEntity);
        Category categoryNew=persistenceAdapter.save(product);

        assertEquals(categoryNew,product);
        Mockito.verify(mapper,times(1)).toCategory(any(CategoryEntity.class));
        Mockito.verify(mapper,times(1)).toCategoryEntity(any(Category.class));
        Mockito.verify(repository,times(1)).save(any(CategoryEntity.class));
    }

    @Test
    void shouldDeleteProductWhenIntoAnIdValid(){
        doNothing().when(repository).deleteById(anyLong());
        persistenceAdapter.delete(1L);
        Mockito.verify(repository,times(1)).deleteById(anyLong());
    }
}
