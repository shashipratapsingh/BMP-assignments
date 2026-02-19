package account.service.controller;
import account.service.dto.AccountDto;
import account.service.dto.AccountViewDto;
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
@RequestMapping("/accounts")
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
    @GetMapping("/api/accounts/user/{userId}")
    @Operation(summary = "View all accounts of a user")
    @ApiResponse(responseCode = "200", description = "Accounts fetched successfully")
    public ResponseEntity<List<AccountViewDto>> getAccountsByUser(
            @PathVariable Long userId) {

        List<Account> accounts = accountService.getAccountsByUserId(userId);

        List<AccountViewDto> response = accounts.stream()
                .map(acc -> new AccountViewDto(
                        acc.getId(),
                        acc.getAccountNumber(),
                        acc.getBalance(),
                        acc.getAccountType()))
                .toList();

        return ResponseEntity.ok(response);
    }


}