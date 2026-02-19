package account.service.repository;
import account.service.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("SELECT p FROM Account p WHERE p.price < ?1")
    List<Account> findByPriceLessThan(double price);
}