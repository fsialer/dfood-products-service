package com.fernando.ms.products.app.dfood_products_service.infrastructure.adapters.input.rest;

import com.fernando.ms.products.app.dfood_products_service.application.ports.input.ProductInputPort;
import com.fernando.ms.products.app.dfood_products_service.infrastructure.adapters.input.rest.mapper.ProductRestMapper;
import com.fernando.ms.products.app.dfood_products_service.infrastructure.adapters.input.rest.models.request.CreateProductRequest;
import com.fernando.ms.products.app.dfood_products_service.infrastructure.adapters.input.rest.models.response.ProductResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductRestAdapter {

    private final ProductInputPort productInputPort;
    private final ProductRestMapper productRestMapper;

    @GetMapping
    public ResponseEntity<List<ProductResponse>> findAll(){
        return ResponseEntity.ok(productRestMapper.toProductsResponse(productInputPort.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> findById(@PathVariable Long id){
        return ResponseEntity.ok(productRestMapper.toProductResponse(productInputPort.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ProductResponse> save(@Valid @RequestBody CreateProductRequest rq){
        ProductResponse productResponse=productRestMapper.toProductResponse(productInputPort.save(productRestMapper.toProduct(rq)));
        return ResponseEntity.created(URI.create("/products/".concat(productResponse.getId().toString()))).body(productResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> update(@PathVariable Long id,@Valid @RequestBody CreateProductRequest rq){
        ProductResponse productResponse=productRestMapper.toProductResponse(productInputPort.update(id,productRestMapper.toProduct(rq)));
        return ResponseEntity.ok().body(productResponse);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        productInputPort.delete(id);
    }

    @GetMapping("find-by-ids")
    public ResponseEntity<List<ProductResponse>> findByIds(@RequestParam List<Long> ids){
        return ResponseEntity.ok(productRestMapper.toProductsResponse(productInputPort.findByIds(ids)));
    }

}
