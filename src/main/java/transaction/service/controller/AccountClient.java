package transaction.service.controller;


import transaction.service.entity.Account;
import org.springframework.web.bind.annotation.*;

import org.springframework.cloud.openfeign.FeignClient;

import java.util.List;

@FeignClient(name = "product-service", url = "${product-service.url}")
public interface AccountClient {

    @PostMapping
    Account createProduct(@RequestBody Account account);

    @GetMapping
    List<Account> getAllProducts();

    @GetMapping("/{id}")
    Account getProductById(@PathVariable("id") Long id);

    @PutMapping("/{id}")
    Account updateProduct(@PathVariable("id") Long id, @RequestBody Account account);

    @DeleteMapping("/{id}")
    void deleteProduct(@PathVariable("id") Long id);
}