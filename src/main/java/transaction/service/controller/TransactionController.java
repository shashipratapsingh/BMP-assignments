package transaction.service.controller;


import transaction.service.entity.Account;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions/accounts")
public class TransactionController {

    @Autowired
    private AccountClient accountClient;

    @GetMapping("/healthCheck")
    @Operation(summary = "Check Health of Account Service")
    @ApiResponse(responseCode = "200", description = "Service is working fine")
    public String healthCheck() {
        return "Transaction service working fine";
    }

    @GetMapping
    @Operation(summary = "Get all products")
    @ApiResponse(responseCode = "200", description = "List of all Account")
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    public ResponseEntity<?> getAllProducts() {
        try {
            List<Account> accounts = accountClient.getAllProducts();
            return ResponseEntity.ok(accounts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch Account. Please try again later.");
        }
    }


    @GetMapping("/{id}")
    @Operation(summary = "Get Account by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Account found"),
            @ApiResponse(responseCode = "404", description = "Account not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        try {
            Account account = accountClient.getProductById(id);
            return account != null
                    ? ResponseEntity.ok(account)
                    : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found with ID: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while fetching the Account. Please try again later.");
        }
    }


    @PutMapping("/{id}")
    @Operation(summary = "Update a Account")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Account updated successfully"),
            @ApiResponse(responseCode = "404", description = "Account not found"),
            @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    public ResponseEntity<Account> updateProduct(@PathVariable Long id, @Valid @RequestBody Account account) {
        Account updatedAccount = accountClient.updateProduct(id, account); // This throws AccountNotFoundException if not found
        return ResponseEntity.ok(updatedAccount);
    }



    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a Account")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Account deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        accountClient.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

}