package achraf.ebankbackend.services;

import achraf.ebankbackend.dtos.*;
import achraf.ebankbackend.entities.BankAccount;
import achraf.ebankbackend.exceptions.BalanceNotSufficientException;
import achraf.ebankbackend.exceptions.BankAccountNotFoundException;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;

public interface BankAccountService {

    CustomerDTO saveCustomer(CustomerDTO customer);

    CustomerDTO updateCustomer(CustomerDTO customer);

    void deleteCustomer(Long customerId);

    CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId);
    SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId);

    List<CustomerDTO> listCustomers(String keyword);
    BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException;
    void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException;
    void credit(String accountId, double amount, String description) throws BankAccountNotFoundException;
    void transfer(String fromAccountId, String toAccountId, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException;

    List<BankAccountDTO> bankAccountList();

    CustomerDTO getCustomer(Long customerId);

//    List<OperationDTO> accountHistory(String accountId);

    AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException, AccountNotFoundException;


}
