package com.fernando.ms.products.app.dfood_products_service.application.services;

import com.fernando.ms.products.app.dfood_products_service.application.ports.output.CategoryPersistencePort;
import com.fernando.ms.products.app.dfood_products_service.domain.exceptions.CategoryNotFoundException;
import com.fernando.ms.products.app.dfood_products_service.domain.model.Category;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @Mock
    private CategoryPersistencePort categoryPersistencePort;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void shouldReturnAllCategoriesWhenListIsNotEmpty(){
        when(categoryPersistencePort.findAll())
                .thenReturn(Collections.singletonList(TestUtils.buildCategoryMock()));
        List<Category> categories=categoryService.findAll();
        assertEquals(1,categories.size());
        Mockito.verify(categoryPersistencePort,times(1)).findAll();
    }

    @Test
    void shouldReturnAllCategoriesWhenListIsEmpty(){
        when(categoryPersistencePort.findAll())
                .thenReturn(Collections.emptyList());
        List<Category> categories=categoryService.findAll();
        assertEquals(0,categories.size());
        Mockito.verify(categoryPersistencePort,times(1)).findAll();
    }

    @Test
    void shouldReturnOneCategoryWhenIntoAnId(){
        when(categoryPersistencePort.findById(anyLong()))
                .thenReturn(Optional.of(TestUtils.buildCategoryMock()));
        Category category=categoryService.findById(1L);
        assertNotNull(category);

        Mockito.verify(categoryPersistencePort,times(1)).findById(anyLong());
    }

    @Test
    void shouldReturnProductNoFoundWhenIntoAnIdNotExisting(){
        when(categoryPersistencePort.findById(anyLong()))
                .thenReturn(Optional.empty());
        assertThrows(CategoryNotFoundException.class,()->categoryService.findById(2L));

        Mockito.verify(categoryPersistencePort,times(1)).findById(anyLong());
    }

    @Test
    void shouldReturnCategoryWhenSaveAnCategoryNew(){
        Category categoryNew=TestUtils.buildCategoryMock();

        when(categoryPersistencePort.save(any(Category.class)))
                .thenReturn(categoryNew);

        Category category=categoryService.save(categoryNew);
        assertNotNull(category);
        Mockito.verify(categoryPersistencePort,times(1)).save(any(Category.class));
    }

    @Test
    void shouldReturnCategoryWhenUpdateAnProductById(){
        Category categoryNew=TestUtils.buildCategoryMock();

        when(categoryPersistencePort.findById(anyLong()))
                .thenReturn(Optional.of(categoryNew));
        when(categoryPersistencePort.save(any(Category.class)))
                .thenReturn(categoryNew);

        Category categoryUpdated=categoryService.update(1L,categoryNew);

        assertEquals(categoryNew.getName(),categoryUpdated.getName());

        Mockito.verify(categoryPersistencePort,times(1)).findById(anyLong());
        Mockito.verify(categoryPersistencePort,times(1)).save(any(Category.class));
    }

    @Test
    void shouldReturnCategoryNotFoundWhenUpdateAnProductByIdNotValid(){
        Category categoryNew=TestUtils.buildCategoryMock();
        when(categoryPersistencePort.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class,()->categoryService.update(2L,categoryNew));

        Mockito.verify(categoryPersistencePort,times(1)).findById(anyLong());
        Mockito.verify(categoryPersistencePort,times(0)).save(any(Category.class));
    }

    @Test
    void shouldDeleteProductWhenAnProductByIdExist(){
        when(categoryPersistencePort.findById(anyLong()))
                .thenReturn(Optional.of(TestUtils.buildCategoryMock()));
        doNothing().when(categoryPersistencePort).delete(anyLong());
        categoryService.delete(1L);

        Mockito.verify(categoryPersistencePort,times(1)).findById(anyLong());
        Mockito.verify(categoryPersistencePort,times(1)).delete(anyLong());
    }

    @Test
    void shouldNotDeleteProductWhenAnProductByIdNoExist(){
        when(categoryPersistencePort.findById(anyLong()))
                .thenReturn(Optional.empty());
        assertThrows(CategoryNotFoundException.class,()->categoryService.delete(1L));
        Mockito.verify(categoryPersistencePort,times(1)).findById(anyLong());
        Mockito.verify(categoryPersistencePort,times(0)).delete(anyLong());
    }

}
