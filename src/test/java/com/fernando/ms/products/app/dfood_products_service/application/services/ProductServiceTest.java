package com.fernando.ms.products.app.dfood_products_service.application.services;


import com.fernando.ms.products.app.dfood_products_service.application.ports.output.CategoryPersistencePort;
import com.fernando.ms.products.app.dfood_products_service.application.ports.output.ProductPersistencePort;
import com.fernando.ms.products.app.dfood_products_service.application.services.strategy.product.IUpdateStockProductStrategy;
import com.fernando.ms.products.app.dfood_products_service.domain.exceptions.CategoryNotFoundException;
import com.fernando.ms.products.app.dfood_products_service.domain.exceptions.OutputStockRuleException;
import com.fernando.ms.products.app.dfood_products_service.domain.exceptions.ProductNotFoundException;
import com.fernando.ms.products.app.dfood_products_service.domain.exceptions.UpdateStockStrategyNotFoundException;
import com.fernando.ms.products.app.dfood_products_service.domain.model.Category;
import com.fernando.ms.products.app.dfood_products_service.domain.model.Product;
import com.fernando.ms.products.app.dfood_products_service.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductPersistencePort productPersistencePort;

    @Mock
    private CategoryPersistencePort categoryPersistencePort;

    @InjectMocks
    private ProductService productService;

    @Mock
    private IUpdateStockProductStrategy updateStockProductStrategy;

    private List<IUpdateStockProductStrategy> updateStockProductStrategyList;

    @BeforeEach
    void setUp() {
        updateStockProductStrategyList = List.of(updateStockProductStrategy);
        productService = new ProductService(productPersistencePort, categoryPersistencePort, updateStockProductStrategyList);
    }

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
    void shouldReturnCategoryNotWhenSaveAnProductNew(){
        Product productNew=TestUtils.buildProductMock();
        Category category=TestUtils.buildCategoryMock();
        when(categoryPersistencePort.findById(anyLong()))
                .thenReturn(Optional.empty());
        assertThrows(CategoryNotFoundException.class,()->productService.save(productNew));
        Mockito.verify(categoryPersistencePort,times(1)).findById(anyLong());
        Mockito.verify(productPersistencePort,times(0)).save(any(Product.class));
    }

    @Test
    void shouldReturnProductWhenSaveAnProductNew(){
        Product productNew=TestUtils.buildProductMock();
        Category category=TestUtils.buildCategoryMock();
        when(categoryPersistencePort.findById(anyLong()))
                .thenReturn(Optional.of(category));
        when(productPersistencePort.save(any(Product.class)))
                .thenReturn(productNew);
        Product product=productService.save(productNew);
        assertNotNull(product);
        Mockito.verify(productPersistencePort,times(1)).save(any(Product.class));
        Mockito.verify(categoryPersistencePort,times(1)).findById(anyLong());
    }

    @Test
    void shouldReturnProductWhenUpdateAnProductById(){
        Product productNew=TestUtils.buildProductMock();
        when(productPersistencePort.findById(anyLong()))
                .thenReturn(Optional.of(productNew));
        when(productPersistencePort.save(any(Product.class)))
                .thenReturn(productNew);
        Product productUpated=productService.update(1L,productNew);
        assertEquals(productUpated.getName(),productNew.getName());
        assertEquals(productUpated.getDescription(),productNew.getDescription());
        Mockito.verify(categoryPersistencePort,times(0)).findById(anyLong());
        Mockito.verify(productPersistencePort,times(1)).findById(anyLong());
        Mockito.verify(productPersistencePort,times(1)).save(productNew);
    }

    @Test
    void shouldReturnProductWhenUpdateAnProductAndCategoryById(){
        Product productNew=TestUtils.buildProductMock();
        Product productUpdated=TestUtils.buildProductUpdatedMock();
        Category category=TestUtils.buildCategoryUpdateMock();
        when(categoryPersistencePort.findById(anyLong()))
                .thenReturn(Optional.of(category));
        when(productPersistencePort.findById(anyLong()))
                .thenReturn(Optional.of(productNew));
        when(productPersistencePort.save(any(Product.class)))
                .thenReturn(productNew);
        Product productUpdate=productService.update(1L,productUpdated);
        assertNotNull(productUpdate);
        assertEquals(productUpdated.getId(),productUpdate.getId());
        Mockito.verify(categoryPersistencePort,times(1)).findById(anyLong());
        Mockito.verify(productPersistencePort,times(1)).findById(anyLong());
        Mockito.verify(productPersistencePort,times(1)).save(productNew);
    }

    @Test
    void shouldReturnCategoryNotFoundWhenUpdateAnProductAndCategoryNoExistingById(){
        Product productNew=TestUtils.buildProductMock();
        Product productUpdated=TestUtils.buildProductUpdatedMock();
        when(productPersistencePort.findById(anyLong()))
                .thenReturn(Optional.of(productNew));
        when(categoryPersistencePort.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class,()->productService.update(1L,productUpdated));
        Mockito.verify(categoryPersistencePort,times(1)).findById(anyLong());
        Mockito.verify(productPersistencePort,times(1)).findById(anyLong());
        Mockito.verify(productPersistencePort,times(0)).save(productNew);
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
    @DisplayName("When Products Identifiers Are Correct Expect A List Products")
    void When_ProductsIdentifiersAreCorrects_Expect_AListProducts(){
        List<Long> ids = Collections.singletonList(1L);
           when(productPersistencePort.findByIds(anyCollection()))
                   .thenReturn(Collections.singletonList(TestUtils.buildProductMock()));
        List<Product> products=productService.findByIds(ids);
        assertEquals(1,products.size());
        Mockito.verify(productPersistencePort,times(1)).findByIds(anyCollection());
    }

    @Test
    @DisplayName("When Products Identifiers Are InCorrects Expect A List Void")
    void When_ProductsIdentifiersAreInCorrect_Expect_AListVoid() {
        List<Long> ids = Collections.singletonList(2L);
        when(productPersistencePort.findByIds(anyCollection()))
                .thenReturn(Collections.emptyList());
        List<Product> products = productService.findByIds(ids);
        assertEquals(0, products.size());
        Mockito.verify(productPersistencePort, times(1)).findByIds(anyCollection());
    }

    @Test
    @DisplayName("When Products Identifiers Are Corrects Expect A Return True")
    void When_ProductsIdentifiersAreCorrect_Expect_AReturnTrue() {
        List<Long> ids = Collections.singletonList(1L);
        when(productPersistencePort.findByIds(anyCollection()))
                .thenReturn(Collections.singletonList(TestUtils.buildProductMock()));
        productService.verifyExistsProductByIds(ids);
        Mockito.verify(productPersistencePort, times(1)).findByIds(anyCollection());
    }

    @Test
    @DisplayName("When Products Identifiers Are InCorrects Expect A Return True")
    void Expect_ProductNotFoundException_WhenProductsIdentifiersAreIncorrectForVe() {
        List<Long> ids = Collections.singletonList(2L);
        when(productPersistencePort.findByIds(anyCollection()))
                .thenReturn(Collections.singletonList(TestUtils.buildProductMock()));
        assertThrows(ProductNotFoundException.class,()->productService.verifyExistsProductByIds(ids));
        Mockito.verify(productPersistencePort, times(1)).findByIds(anyCollection());
    }

    @Test
    @DisplayName("When Stock Product Is Input Correct Expect A Return Product")
    void When_StockProductIsInputCorrect_Expect_AReturnProduct() {
        Product product=TestUtils.buildProductMock();

        when(updateStockProductStrategy.isApplicable("INPUT")).thenReturn(true);
        when(updateStockProductStrategy.doOperation(anyInt(),anyInt())).thenReturn(11);

        when(productPersistencePort.findById(anyLong()))
                .thenReturn(Optional.of(product));
        when(productPersistencePort.save(any(Product.class)))
                .thenReturn(product);
        Product productUpdated=productService.updateStock(1L,10,"INPUT");
        assertNotNull(productUpdated);
        assertEquals(11,productUpdated.getQuantity());
        Mockito.verify(productPersistencePort,times(1)).findById(1L);
        Mockito.verify(productPersistencePort,times(1)).save(product);
        verify(updateStockProductStrategy, times(1)).isApplicable(anyString());
        verify(updateStockProductStrategy, times(1)).doOperation(anyInt(),anyInt());
    }

    @Test
    @DisplayName("When Stock Product Is Output Correct Expect A Return Product")
    void When_StockProductIsOutputCorrect_Expect_AReturnProduct() {
        Product product=TestUtils.buildProductMock();

        when(updateStockProductStrategy.isApplicable("OUTPUT")).thenReturn(true);
        when(updateStockProductStrategy.doOperation(anyInt(),anyInt())).thenReturn(9);

        when(productPersistencePort.findById(anyLong()))
                .thenReturn(Optional.of(product));
        when(productPersistencePort.save(any(Product.class)))
                .thenReturn(product);
        Product productUpdated=productService.updateStock(1L,10,"OUTPUT");
        assertNotNull(productUpdated);
        assertEquals(9,productUpdated.getQuantity());
        Mockito.verify(productPersistencePort,times(1)).findById(1L);
        Mockito.verify(productPersistencePort,times(1)).save(product);
        verify(updateStockProductStrategy, times(1)).isApplicable(anyString());
        verify(updateStockProductStrategy, times(1)).doOperation(anyInt(),anyInt());
    }

    @Test
    @DisplayName("Expect Product NotFoundException When Stock Product Is Invalid")
    void Expect_ProductNotFoundException_WhenStockProductIsInvalid(){
        Product product=TestUtils.buildProductMock();
        when(productPersistencePort.findById(anyLong()))
                .thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class,()->productService.updateStock(1L,10,"INPUT"));
        Mockito.verify(productPersistencePort,times(1)).findById(1L);
    }

    @Test
    @DisplayName("Expect UpdateStockStrategyNotFoundException When Operation Stock Product Is Invalid")
    void Expect_UpdateStockStrategyNotFoundException_WhenOperationStockProductIsInvalid(){
        Product product = TestUtils.buildProductMock();
        when(productPersistencePort.findById(anyLong())).thenReturn(Optional.of(product));
        when(updateStockProductStrategy.isApplicable("ANY")).thenReturn(false); // Cambiar a false

        UpdateStockStrategyNotFoundException exception = assertThrows(UpdateStockStrategyNotFoundException.class, () -> {
            productService.updateStock(1L, 10, "ANY");
        });
        assertEquals("Operation not valid: ANY", exception.getMessage());

        Mockito.verify(productPersistencePort, times(1)).findById(1L);
        Mockito.verify(updateStockProductStrategy, times(1)).isApplicable("ANY");
    }

    @Test
    @DisplayName("Expect OutputStockRuleException When Quantity To Output Is Greater Than Current Stock")
    void Expect_OutputStockRuleException_WhenQuantityToOutputIsGreaterThanCurrentStock() {
        Product product = TestUtils.buildProductMock();
        product.setQuantity(5); // Set current stock to 5

        when(productPersistencePort.findById(anyLong())).thenReturn(Optional.of(product));
        when(updateStockProductStrategy.isApplicable("OUTPUT")).thenReturn(true);
        doThrow(new OutputStockRuleException("Quantity to output is greater than current stock")).when(updateStockProductStrategy).doOperation(anyInt(), anyInt());

        OutputStockRuleException exception = assertThrows(OutputStockRuleException.class, () -> {
            productService.updateStock(1L, 10, "OUTPUT");
        });
        assertEquals("Quantity to output is greater than current stock", exception.getMessage());

        Mockito.verify(productPersistencePort, times(1)).findById(1L);
        Mockito.verify(updateStockProductStrategy, times(1)).isApplicable("OUTPUT");
        Mockito.verify(updateStockProductStrategy, times(1)).doOperation(anyInt(), anyInt());
    }

    @Test
    @DisplayName("Expect OutputStockRuleException When Current Stock Is Zero")
    void Expect_OutputStockRuleException_When_CurrentStockIsZero() {
        Product product = TestUtils.buildProductMock();
        product.setQuantity(0); // Set current stock to 5

        when(productPersistencePort.findById(anyLong())).thenReturn(Optional.of(product));
        when(updateStockProductStrategy.isApplicable("OUTPUT")).thenReturn(true);
        doThrow(new OutputStockRuleException("Current stock is zero")).when(updateStockProductStrategy).doOperation(anyInt(), anyInt());

        OutputStockRuleException exception = assertThrows(OutputStockRuleException.class, () -> {
            productService.updateStock(1L, 10, "OUTPUT");
        });
        assertEquals("Current stock is zero", exception.getMessage());

        Mockito.verify(productPersistencePort, times(1)).findById(1L);
        Mockito.verify(updateStockProductStrategy, times(1)).isApplicable("OUTPUT");
        Mockito.verify(updateStockProductStrategy, times(1)).doOperation(anyInt(), anyInt());
    }


}
