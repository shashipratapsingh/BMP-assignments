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
@Transactional
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountNumberGenerator generator;

    @Override
    public Account createAccount(Account account) {

        try {
            String accNumber;
            do {
                accNumber = generator.generateAccountNumber();
            } while (accountRepository.existsByAccountNumber(accNumber));

            account.setAccountNumber(accNumber);
            account.setBalance(1000.00);

            return accountRepository.save(account);

        } catch (Exception ex) {
            throw new RuntimeException("Unable to create account");
        }
    }

    @Override
    public List<Account> getAccountsByUserId(Long userId) {
        List<Account> accounts = accountRepository.findByUserId(userId);
        if (accounts.isEmpty()) {
            throw new AccountNotFoundException(
                    "No accounts found for userId : " + userId);
        }
        return accounts;
    }
}
