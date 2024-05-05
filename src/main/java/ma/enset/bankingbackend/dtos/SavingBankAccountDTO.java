package ma.enset.bankingbackend.dtos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.enset.bankingbackend.entities.AccountOperation;
import ma.enset.bankingbackend.entities.Customer;
import ma.enset.bankingbackend.enums.AccountStatus;

import java.util.Date;
import java.util.List;

@Data
public  class SavingBankAccountDTO extends BankAccountDTO{

    private String id;
    private double balance;
    private Date createdAt;
    private AccountStatus status;
    private CustomerDTO customerDTO;
    private double interestRate;

}
