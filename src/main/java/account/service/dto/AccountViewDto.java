package account.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountViewDto {

    private Long id;
    private String accountNumber;
    private Double balance;
    private String accountType;
}
