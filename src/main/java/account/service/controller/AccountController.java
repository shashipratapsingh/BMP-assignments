package account.service.controller;
import account.service.dto.AccountDto;
import account.service.entity.Account;
import account.service.exception.AccountNotFoundException;
import account.service.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import account.service.dto.ErrorResponse;
import java.util.List;


@RestController
@RequestMapping("/products")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/healthCheck")
    @Operation(summary = "Check Health of Account Service")
    @ApiResponse(responseCode = "200", description = "Service is working fine")
    public String healthCheck() {
        return "Account service working fine";
    }

    @PostMapping
    @Operation(summary = "Create a new Account")
    @ApiResponse(responseCode = "201", description = "Account created successfully")
    public ResponseEntity<AccountDto> createAccount(@Valid @RequestBody Account account) {
        try {
            Account createdAccount = accountService.createAccount(account);
            AccountDto response = new AccountDto(
                    createdAccount.getId(),
                    HttpStatus.CREATED.value(),
                    "Account saved successfully"
            );

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            AccountDto errorResponse = new AccountDto(
                    null,
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Failed to save Account: " + e.getMessage()
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    @Operation(summary = "Get all Account")
    @ApiResponse(responseCode = "200", description = "List of all Account")
    @ApiResponse(responseCode = "404", description = "No Account found")
    public ResponseEntity<List<Account>> getAllProducts() {
        List<Account> account = accountService.getAllAccount();
        if (account.isEmpty()) {
            throw new AccountNotFoundException("No Account found in the database.");
        }
        return new ResponseEntity<>(account, HttpStatus.OK);
    }



    @GetMapping("/{id}")
    @Operation(summary = "Get Account by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Account found"),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    public ResponseEntity<Object> getProductById(@PathVariable Long id) {
        try {
            Account account = accountService.getAccountById(id).orElseThrow(() -> new AccountNotFoundException("Product with ID " + id + " not found"));
            return new ResponseEntity<>(account, HttpStatus.OK);  // Return the product if found
        } catch (AccountNotFoundException ex) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), "Account with ID " + id + " not found in the database");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping("/{id}")
    @Operation(summary = "Update a Account")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Account updated successfully"),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    public ResponseEntity<Object> updateProduct(@PathVariable Long id, @Valid @RequestBody Account account) {
        try {
            Account updatedAccount = accountService.updateAccount(id, account);
            if (updatedAccount != null) {
                return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
            } else {
                throw new AccountNotFoundException("Account with ID " + id + " not found for update");
            }
        } catch (AccountNotFoundException ex) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a Account")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Account deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    public ResponseEntity<Object> deleteProduct(@PathVariable Long id) {
        try {
            boolean isDeleted = accountService.deleteAccount(id);
            if (isDeleted) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                throw new AccountNotFoundException("Account with ID " + id + " not found for deletion");
            }
        } catch (AccountNotFoundException ex) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping("/batchUpdate")
    @Operation(summary = "Update multiple Account")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Account updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid product ID in the list")
    })
    public ResponseEntity<Object> updateMultipleProducts(@RequestBody List<Account> products) {
        try {
            accountService.updateMultipleAccount(products);
            return new ResponseEntity<>("Account updated successfully", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

}