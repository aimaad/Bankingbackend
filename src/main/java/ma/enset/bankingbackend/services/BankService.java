package ma.enset.bankingbackend.services;

import ma.enset.bankingbackend.entities.BankAccount;
import ma.enset.bankingbackend.entities.CurrentAccount;
import ma.enset.bankingbackend.entities.SavingAccount;
import ma.enset.bankingbackend.repository.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BankService {
    @Autowired
    private BankAccountRepository bankAccountRepository;

    public void consulter(){
        BankAccount bankAccount = bankAccountRepository.findById("1aec8237-9755-401e-ac20-b6202999f5a5").orElse(null);
        if(bankAccount!=null) {
            System.out.println("*****************************************************");
            System.out.println(bankAccount.getId());
            System.out.println(bankAccount.getBalance());
            System.out.println(bankAccount.getStatus());
            System.out.println(bankAccount.getCreatedAt());
            System.out.println(bankAccount.getCustomer().getName());
            if (bankAccount instanceof CurrentAccount) {
                System.out.println("Over draft => " + ((CurrentAccount) bankAccount).getOverDraft());
            }
            if (bankAccount instanceof SavingAccount) {
                System.out.println("Over draft => " + ((SavingAccount) bankAccount).getInterestRate());
            }
            bankAccount.getAccountOperations().forEach(op -> {
                System.out.println(op.getType() + "\t" + op.getOperationDate() + "\t" + op.getAmount());
            });
        }
    }
}
