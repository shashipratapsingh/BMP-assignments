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
    List<Account> getAllAccount();

    @GetMapping("/{id}")
    Account getAccountById(@PathVariable("id") Long id);

    @PutMapping("/{id}")
    Account updateAccount(@PathVariable("id") Long id, @RequestBody Account account);

    @DeleteMapping("/{id}")
    void deleteAccount(@PathVariable("id") Long id);
}