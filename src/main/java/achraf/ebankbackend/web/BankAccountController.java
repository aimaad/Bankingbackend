package achraf.ebankbackend.web;

import achraf.ebankbackend.dtos.*;
import achraf.ebankbackend.exceptions.BalanceNotSufficientException;
import achraf.ebankbackend.exceptions.BankAccountNotFoundException;
import achraf.ebankbackend.services.BankAccountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;

@RestController
@CrossOrigin("*")
@AllArgsConstructor
public class BankAccountController {
    private BankAccountService bankAccountService;

//    @GetMapping("/accounts/{accountID}")
//    public BankAccountDTO getBankAccount(@PathVariable String accountID) throws BankAccountNotFoundException {
//        return bankAccountService.getBankAccount(accountID);
//    }

    @GetMapping("/accounts")
    public List<BankAccountDTO> listAccounts(
    ){
        return bankAccountService.bankAccountList();
    }


    @GetMapping("/accounts/{accountId}")
    public AccountHistoryDTO getAccountHistory(
            @PathVariable String accountId,
            @RequestParam(name = "pageOperations", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size) throws BankAccountNotFoundException, AccountNotFoundException {
        return bankAccountService.getAccountHistory(accountId, page, size);
    }


    @PostMapping("/accounts/debit")
    public DebitDTO debit(@RequestBody DebitDTO debitDTO) throws BalanceNotSufficientException, BankAccountNotFoundException {
        this.bankAccountService.debit(debitDTO.getAccountId(), debitDTO.getAmount(), debitDTO.getDescription());
        return debitDTO;
    }

    @PostMapping("/accounts/credit")
    public CreditDTO credit(@RequestBody CreditDTO creditDTO) throws AccountNotFoundException, BankAccountNotFoundException {
        this.bankAccountService.credit(creditDTO.getAccountId(), creditDTO.getAmount(), creditDTO.getDescription());
        return creditDTO;
    }

    @PostMapping("/accounts/transfer")
    public void transfer(@RequestBody TransferRequestDTO transferRequestDTO) throws AccountNotFoundException, BalanceNotSufficientException, BankAccountNotFoundException {
        this.bankAccountService.transfer(
                transferRequestDTO.getAccountSource(),
                transferRequestDTO.getAccountDestination(),
                transferRequestDTO.getAmount());
    }


}
