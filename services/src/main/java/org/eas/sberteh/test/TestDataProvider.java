package org.eas.sberteh.test;

import org.eas.sberteh.entity.Account;
import org.eas.sberteh.entity.AccountOperation;
import org.eas.sberteh.repository.AccountOperationRepository;
import org.eas.sberteh.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigInteger;
import java.util.Date;

import static org.eas.sberteh.entity.AccountOperation.Status.DONE;
import static org.eas.sberteh.entity.AccountOperation.Status.REGISTERED;

/**
 * @author aesipov.
 */
@Component
public class TestDataProvider {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountOperationRepository accountOperationRepository;

    @PostConstruct
    public void fillTestData() {
        accountRepository.save(new Account(1L, BigInteger.valueOf(10)));
        accountRepository.save(new Account(2L, BigInteger.valueOf(20)));
        accountRepository.save(new Account(3L, BigInteger.valueOf(30)));

        accountOperationRepository.save(new AccountOperation(1L, 1L, 2L, BigInteger.valueOf(3), new Date(), REGISTERED));
        accountOperationRepository.save(new AccountOperation(2L, 2L, 3L, BigInteger.valueOf(3), new Date(), DONE));

        accountRepository.findAll().forEach(System.out::println);
        accountOperationRepository.findAll().forEach(System.out::println);
    }
}
