package achraf.ebankbackend;

import achraf.ebankbackend.entities.*;
import achraf.ebankbackend.repos.CustomerRepo;
import achraf.ebankbackend.repos.OperationRepo;
import achraf.ebankbackend.repos.BankAccountRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;

import static achraf.ebankbackend.entities.AccountStatus.CREATED;
import static achraf.ebankbackend.entities.OperationType.CREDIT;
import static achraf.ebankbackend.entities.OperationType.DEBIT;

@SpringBootApplication
public class EbankBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbankBackendApplication.class, args);
    }


    @Bean
    CommandLineRunner start(CustomerRepo customerRepo,
                            BankAccountRepo bankAccountRepo,
                            OperationRepo operationRepo){
        return args -> {
            Stream.of("Achraf", "HAMMI", "Rachid").forEach(name ->{
                customerRepo.save(new Customer(null, name, name+"@gmail.com", null));
            });

            customerRepo.findAll().forEach(cust->{
                CurrentAccount currentAccount = new CurrentAccount();
                currentAccount.setId(UUID.randomUUID().toString());
                currentAccount.setBalance(Math.random()*90000);
                currentAccount.setCreatedAt(new Date());
                currentAccount.setStatus(CREATED);
                currentAccount.setCustomer(cust);
                currentAccount.setOverdraft(9000);
                bankAccountRepo.save(currentAccount);


                SavingAccount savingAccount = new SavingAccount();
                savingAccount.setId(UUID.randomUUID().toString());
                savingAccount.setBalance(Math.random()*90000);
                savingAccount.setCreatedAt(new Date());
                savingAccount.setStatus(CREATED);
                savingAccount.setCustomer(cust);
                savingAccount.setInterestRate(5.5);
                bankAccountRepo.save(savingAccount);
            });

            bankAccountRepo.findAll().forEach(acc ->{
                for(int i=0; i<5;i++){
                    Operation operation = new Operation();
                    operation.setOperationDate(new Date());
                    operation.setAmount(Math.random()*1000);
                    operation.setOperationType(Math.random()>0.5? DEBIT:CREDIT);
                    operation.setBankAccount(acc);
                    operationRepo.save(operation);
                }
            });
        };
    }
}
