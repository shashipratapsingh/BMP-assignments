package account.service.service.impl;

import account.service.entity.Account;
import account.service.exception.AccountNotFoundException;
import account.service.repository.AccountRepository;
import account.service.service.AccountService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;


    @Override
    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    public List<Account> getAllAccount() {
        List<Account> productList= accountRepository.findAll();
        if (productList.isEmpty()) {
            throw new AccountNotFoundException("No Account found");
        }
        return productList;
    }
    public Optional<Account> getAccountById(Long id) {
        return Optional.ofNullable(accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException("Account with ID " + id + " not Found")));
    }

    @Transactional
    public Account updateAccount(Long id, Account account) {
        Account existingProduct = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account with ID " + id + " not found"));
        account.setId(existingProduct.getId());
        return accountRepository.save(account);
    }
    @Transactional
    public void updateMultipleAccount(List<Account> accounts) {
        for (Account account1 : accounts) {
            if (!accountRepository.existsById(account1.getId())) {
                throw new IllegalArgumentException(
                        "Account with ID " + account1.getId() + " does not exist");
            }
            accountRepository.save(account1);
        }
    }
    @Transactional
    public boolean deleteAccount(Long id) {
        if (accountRepository.existsById(id)) {
            accountRepository.deleteById(id);
            return true;
        }else {
            throw new AccountNotFoundException("Account with ID " + id + " does not exist");
        }
    }
}
