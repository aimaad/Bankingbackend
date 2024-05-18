package achraf.ebankbackend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class Operation {
    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private Date operationDate;
    private double amount;
    @Enumerated(STRING)
    private OperationType operationType;
    private String description;
    @ManyToOne
    private BankAccount bankAccount;
}
