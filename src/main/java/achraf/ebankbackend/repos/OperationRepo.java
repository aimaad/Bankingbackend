package achraf.ebankbackend.repos;

import achraf.ebankbackend.entities.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OperationRepo extends JpaRepository<Operation, Long> {
//    Page<Operation> findByBankAccountId(String accountId, PageRequest of);

    Page<Operation> findByBankAccountIdOrderByOperationDateDesc(String accountId, PageRequest of);


}
