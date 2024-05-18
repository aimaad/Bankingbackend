package achraf.ebankbackend.web;

import achraf.ebankbackend.dtos.CustomerDTO;
import achraf.ebankbackend.entities.Customer;
import achraf.ebankbackend.services.BankAccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@CrossOrigin("*")
public class CustomerController {
    private BankAccountService bankAccountService;

    @GetMapping("/customers/search")
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    private List<CustomerDTO> customers(@RequestParam(name= "keyword") String keyword){
        return bankAccountService.listCustomers(keyword);
    }

    @GetMapping("/customers/{customerId}")
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public CustomerDTO getCustomer(@PathVariable(name = "customerId") Long customerId){
        return bankAccountService.getCustomer(customerId);
    }

    @PostMapping("/customers/create")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customer){
        return bankAccountService.saveCustomer(customer);
    }

    @PutMapping("/customers/{customerId}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public CustomerDTO updateCustomer(@PathVariable(name = "customerId") Long customerId, @RequestBody CustomerDTO customer){
        customer.setId(customerId);
        return bankAccountService.updateCustomer(customer);
    }

    @DeleteMapping("/customers/{customerId}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public void deleteCustomer(@PathVariable(name = "customerId") Long customerId){
        bankAccountService.deleteCustomer(customerId);
    }
}
