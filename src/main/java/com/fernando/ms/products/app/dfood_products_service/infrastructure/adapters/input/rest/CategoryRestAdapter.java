package com.fernando.ms.products.app.dfood_products_service.infrastructure.adapters.input.rest;

import com.fernando.ms.products.app.dfood_products_service.application.ports.input.CategoryInputPort;
import com.fernando.ms.products.app.dfood_products_service.infrastructure.adapters.input.rest.mapper.CategoryRestMapper;
import com.fernando.ms.products.app.dfood_products_service.infrastructure.adapters.input.rest.models.request.CreateCategoryRequest;
import com.fernando.ms.products.app.dfood_products_service.infrastructure.adapters.input.rest.models.response.CategoryResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryRestAdapter {

    private final CategoryInputPort categoryInputPort;
    private final CategoryRestMapper categoryRestMapper;

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> findAll(){
        return ResponseEntity.ok(categoryRestMapper.toCategoriesResponse(categoryInputPort.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> findById(@PathVariable Long id){
        return ResponseEntity.ok(categoryRestMapper.toCategoryResponse(categoryInputPort.findById(id)));
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> save(@Valid @RequestBody CreateCategoryRequest rq){
        CategoryResponse productResponse=categoryRestMapper.toCategoryResponse(categoryInputPort.save(categoryRestMapper.toCategory(rq)));
        return ResponseEntity.created(URI.create("/products/".concat(productResponse.getId().toString()))).body(productResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> update(@PathVariable Long id,@Valid @RequestBody CreateCategoryRequest rq){
        CategoryResponse productResponse=categoryRestMapper.toCategoryResponse(categoryInputPort.update(id,categoryRestMapper.toCategory(rq)));
        return ResponseEntity.ok().body(productResponse);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        categoryInputPort.delete(id);
    }
}
