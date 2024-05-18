package achraf.ebankbackend.services;

import achraf.ebankbackend.dtos.*;
import achraf.ebankbackend.entities.*;
import achraf.ebankbackend.exceptions.BalanceNotSufficientException;
import achraf.ebankbackend.exceptions.BankAccountNotFoundException;
import achraf.ebankbackend.exceptions.CustomerNotFoundException;
import achraf.ebankbackend.mappers.BankAccountMapperImpl;
import achraf.ebankbackend.repos.BankAccountRepo;
import achraf.ebankbackend.repos.CustomerRepo;
import achraf.ebankbackend.repos.OperationRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.AccountNotFoundException;
import java.awt.print.Pageable;
import java.util.List;
import java.util.UUID;

import static achraf.ebankbackend.entities.OperationType.CREDIT;
import static achraf.ebankbackend.entities.OperationType.DEBIT;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankAccountServiceImpl implements BankAccountService {
    private CustomerRepo customerRepo;
    private BankAccountRepo bankAccountRepo;
    private OperationRepo operationRepo;
    private BankAccountMapperImpl dtoMapper;

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customer) {
        log.info("saving new customer");
        return dtoMapper.fromCustomer(customerRepo.save(dtoMapper.fromCustomerDTO(customer)));
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customer) {
        log.info("updating new customer");
        return dtoMapper.fromCustomer(customerRepo.save(dtoMapper.fromCustomerDTO(customer)));
    }

    @Override
    public void deleteCustomer(Long customerId){
        customerRepo.deleteById(customerId);
    }

    @Override
    public CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepo.findById(customerId).orElse(null);
        if(customer == null){
            throw new CustomerNotFoundException("Customer not found");
        }
        CurrentAccount currentBankAccount = new CurrentAccount();
        currentBankAccount.setId(UUID.randomUUID().toString());
        currentBankAccount.setBalance(initialBalance);
        currentBankAccount.setCreatedAt(new java.util.Date());
        currentBankAccount.setCustomer(customer);
        currentBankAccount.setOverdraft(overDraft);
        return dtoMapper.fromCurrentBankAccount(bankAccountRepo.save(currentBankAccount));
    }

    @Override
    public SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepo.findById(customerId).orElse(null);
        if(customer == null){
            throw new CustomerNotFoundException("Customer not found");
        }
        SavingAccount savingBankAccount = new SavingAccount();
        savingBankAccount.setId(UUID.randomUUID().toString());
        savingBankAccount.setBalance(initialBalance);
        savingBankAccount.setCreatedAt(new java.util.Date());
        savingBankAccount.setCustomer(customer);
        savingBankAccount.setInterestRate(interestRate);
        return dtoMapper.fromSavingBankACcount(bankAccountRepo.save(savingBankAccount));
    }

    @Override
    public List<CustomerDTO> listCustomers(String keyword) {
        return customerRepo.findByNameContains(keyword).stream().map(dtoMapper::fromCustomer).toList();
    }

    @Override
    public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException {
        BankAccount bankAccount =
                bankAccountRepo.findById(accountId).orElseThrow(() -> new BankAccountNotFoundException("Bank account not " +
                        "found"));
        if(bankAccount instanceof CurrentAccount) {
            return dtoMapper.fromCurrentBankAccount((CurrentAccount) bankAccount);
        } else if(bankAccount instanceof SavingAccount) {
            return dtoMapper.fromSavingBankACcount((SavingAccount) bankAccount);
        }
        return null;
    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException {
        BankAccount bankAccount = bankAccountRepo.findById(accountId).orElseThrow(() -> new BankAccountNotFoundException(
                "Bank account not found"));

        if(bankAccount.getBalance() < amount){
            throw new BalanceNotSufficientException("Insufficient balance");
        }
        saveOperation(amount, description, bankAccount, DEBIT);
        bankAccount.setBalance(bankAccount.getBalance() - amount);
        bankAccountRepo.save(bankAccount);
    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepo.findById(accountId).orElseThrow(() -> new BankAccountNotFoundException(
                "Bank account not found"));
        saveOperation(amount, description, bankAccount, CREDIT);
        bankAccount.setBalance(bankAccount.getBalance() + amount);
        bankAccountRepo.save(bankAccount);
    }

    private void saveOperation(double amount, String description, BankAccount bankAccount, OperationType operationType) {
        Operation operation = new Operation();
        operation.setOperationType(operationType);
        operation.setAmount(amount);
        operation.setOperationDate(new java.util.Date());
        operation.setDescription(description);
        operation.setBankAccount(bankAccount);
        operationRepo.save(operation);
    }

    @Override
    public void transfer(String fromAccountId, String toAccountId, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException {
        debit(fromAccountId, amount, "debit");
        credit(toAccountId, amount, "credit");
    }

    @Override
    public List<BankAccountDTO> bankAccountList() {
        return bankAccountRepo.findAll().stream().map(bankAccount -> {
            if(bankAccount instanceof CurrentAccount) {
                return dtoMapper.fromCurrentBankAccount((CurrentAccount) bankAccount);
            } else if(bankAccount instanceof SavingAccount) {
                return dtoMapper.fromSavingBankACcount((SavingAccount) bankAccount);
            }
            return null;
        }).toList();
    }

    @Override
    public CustomerDTO getCustomer(Long customerId) {
        return dtoMapper.fromCustomer(customerRepo.findById(customerId).orElseThrow(() -> new CustomerNotFoundException("Customer not found")));

    }

//    @Override
//    public List<OperationDTO> accountHistory(String accountId) {
//        return operationRepo.findByBankAccountId(accountId).stream().map(dtoMapper::fromOperation).toList();
//    }

    @Override
    public AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws AccountNotFoundException {
        BankAccount bankAccount=bankAccountRepo.findById(accountId).orElse(null);
        if(bankAccount==null) throw new AccountNotFoundException("Account not Found");
        Page<Operation> accountOperations = operationRepo.findByBankAccountIdOrderByOperationDateDesc(accountId,
                PageRequest.of(page, size));
        AccountHistoryDTO accountHistoryDTO=new AccountHistoryDTO();
        List<OperationDTO> accountOperationDTOS =
                accountOperations.getContent().stream().map(op -> dtoMapper.fromOperation(op)).toList();
        accountHistoryDTO.setOperationsDTO(accountOperationDTOS);
        accountHistoryDTO.setAccountId(bankAccount.getId());
        accountHistoryDTO.setBalance(bankAccount.getBalance());
        accountHistoryDTO.setCurrentPage(page);
        accountHistoryDTO.setPageSize(size);
        accountHistoryDTO.setTotalPages(accountOperations.getTotalPages());
        return accountHistoryDTO;
    }


}
