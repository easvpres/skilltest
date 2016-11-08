package org.eas.sberteh.service;

import org.eas.sberteh.ServiceApplication;
import org.eas.sberteh.entity.Account;
import org.eas.sberteh.entity.AccountOperation;
import org.eas.sberteh.repository.AccountOperationRepository;
import org.eas.sberteh.repository.AccountRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static org.eas.sberteh.entity.AccountOperation.Status.REGISTERED;

/**
 * @author aesipov.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ServiceApplication.class)
@TestPropertySource(locations="classpath:test.properties")
public class IntegrationTest {
    public static final int ACCOUNT_COUNT = 100;
    public static final int ACCOUNT_OPERATIONS_COUNT = 5000;
    public static final int SLEEP = 15000;

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountOperationRepository accountOperationRepository;

    private Random random = new Random();

    @Test
    public void balanceIsPositive() throws Exception {
        List<Account> accounts = new ArrayList<>();
        for (int i = 0; i < ACCOUNT_COUNT; i++) {
            accounts.add(new Account((long)i, BigInteger.valueOf(random.nextInt(30))));
        }
        accountRepository.save(accounts);

        List<AccountOperation> accountOperations = new ArrayList<>();

        for (int i = 0; i <  ACCOUNT_OPERATIONS_COUNT; i++) {
            int from = random.nextInt(ACCOUNT_COUNT);
            int to = random.nextInt(ACCOUNT_COUNT);
            BigInteger amount = BigInteger.valueOf(random.nextInt(30));
            accountOperations.add(new AccountOperation(null, (long) from, (long) to, amount, new Date(), REGISTERED));
        }
        accountOperationRepository.save(accountOperations);

        Thread.sleep(SLEEP);

        int i = 0;
        for (AccountOperation accountOperation : accountOperationRepository.findAll()) {
            if (accountOperation.getStatus() == REGISTERED) {
                i++;
            }
        }
        System.out.println("not processed = " + i);
        Assert.assertTrue(i < ACCOUNT_OPERATIONS_COUNT);

        accountRepository.findAll().forEach(account -> {
            Assert.assertTrue(account.getBalance().compareTo(BigInteger.ZERO) >= 0);
            System.out.println("account = " + account);
        });
    }
}