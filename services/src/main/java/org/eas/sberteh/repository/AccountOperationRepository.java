package org.eas.sberteh.repository;


import org.eas.sberteh.entity.AccountOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author aesipov.
 */
@Repository
public interface AccountOperationRepository extends JpaRepository<AccountOperation, Long> {
    List<AccountOperation> findByFromBankAccountNumberOrToBankAccountNumber(Long number1, Long number2);
    List<AccountOperation> findByStatus(AccountOperation.Status status, Pageable pageable);
}
