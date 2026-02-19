package account.service.service;


import account.service.entity.Account;
import java.util.List;
import java.util.Optional;


public interface AccountService {

    public Account createAccount(Account account);
    List<Account> getAccountsByUserId(Long userId);
}