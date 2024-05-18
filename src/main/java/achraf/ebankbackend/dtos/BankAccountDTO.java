package achraf.ebankbackend.dtos;

import achraf.ebankbackend.entities.AccountStatus;
import lombok.Data;

import java.util.Date;

@Data
public class BankAccountDTO {
    private String id;
    private double balance;
    private Date createdAt;
    private AccountStatus status;

    private CustomerDTO customer;

}
