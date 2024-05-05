package ma.enset.bankingbackend.dtos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.enset.bankingbackend.entities.BankAccount;
import ma.enset.bankingbackend.enums.OperationType;

import java.util.Date;


@Data
public class AccountOperationDTO {
     private Long id;
    private Date operationDate;
    private double amount;
    private OperationType type;
    private String description;
}
