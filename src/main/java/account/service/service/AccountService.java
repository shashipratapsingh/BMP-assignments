package account.service.service;


import account.service.entity.Account;
import java.util.List;
import java.util.Optional;


public interface AccountService {

    public Account createAccount(Account account);

    public List<Account> getAllAccount();
    public Optional<Account> getAccountById(Long id);
    public Account updateAccount(Long id, Account account);
    public void updateMultipleAccount(List<Account> account);

    public boolean deleteAccount(Long id);
}