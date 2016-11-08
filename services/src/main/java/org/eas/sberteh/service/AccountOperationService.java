package org.eas.sberteh.service;

import org.eas.sberteh.entity.Account;
import org.eas.sberteh.entity.AccountOperation;
import org.eas.sberteh.repository.AccountOperationRepository;
import org.eas.sberteh.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

import static org.eas.sberteh.entity.AccountOperation.Status.REGISTERED;
import static org.springframework.data.domain.Sort.Direction.ASC;

/**
 * @author aesipov.
 */
@Service
public class AccountOperationService {

    private static final Logger logger = LoggerFactory.getLogger(AccountOperationService.class);

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountOperationRepository accountOperationRepository;

    @Transactional
    public List<AccountOperation> findOperationByNumber(Long number) {
        return accountOperationRepository.findByFromBankAccountNumberOrToBankAccountNumber(number, number);
    }

    @Transactional
    public List<AccountOperation> findAllOperations() {
        return accountOperationRepository.findAll();
    }

    @Transactional
    public void register(AccountOperation accountOperation) {
        accountOperation.setNumber(null);
        accountOperation.setTimestamp(new Date());
        accountOperation.setStatus(AccountOperation.Status.REGISTERED);
        accountOperationRepository.save(accountOperation);
    }

    @Transactional
    public void processOperation(AccountOperation accountOperation) {
        logger.info("processOperation: {}", accountOperation);
        Account from = accountRepository.findOne(accountOperation.getFromBankAccountNumber());
        if (from.getBalance().compareTo(accountOperation.getAmount()) < 0) {
            accountOperation.setStatus(AccountOperation.Status.FAILED);
            accountOperationRepository.save(accountOperation);
            return;
        }
        Account to = accountRepository.findOne(accountOperation.getToBankAccountNumber());
        from.setBalance(from.getBalance().subtract(accountOperation.getAmount()));
        to.setBalance(to.getBalance().add(accountOperation.getAmount()));
        accountOperation.setStatus(AccountOperation.Status.DONE);
        accountRepository.save(from);
        accountRepository.save(to);
        accountOperationRepository.save(accountOperation);
    }

    public List<AccountOperation> findByStatusOrderedByTimestamp(AccountOperation.Status status, int maxResultSize) {
        PageRequest pageable = new PageRequest(0, maxResultSize, new Sort(ASC, "timestamp"));
        return accountOperationRepository.findByStatus(REGISTERED, pageable);
    }

    public void save(AccountOperation accountOperation) {
        accountOperationRepository.save(accountOperation);
    }
}
