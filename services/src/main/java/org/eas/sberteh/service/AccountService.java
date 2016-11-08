package org.eas.sberteh.service;

import org.eas.sberteh.entity.Account;
import org.eas.sberteh.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author aesipov.
 */
@Service
public class AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

    @Autowired
    private AccountRepository accountRepository;

    @Transactional
    public Account find(Long number) {
        logger.info("find(number={})", number);
        return accountRepository.findOne(number);
    }

    @Transactional
    public List<Account> findAll() {
        logger.info("findAll");
        return accountRepository.findAll();
    }
}
