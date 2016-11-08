package org.eas.sberteh.controller;

import org.eas.sberteh.entity.Account;
import org.eas.sberteh.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author aesipov.
 */
@RestController
public class AccountController {

    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private AccountService accountService;

    @GetMapping("/account")
    public List<Account> accounts() {
        logger.info("get /account");
        return accountService.findAll();
    }

    @GetMapping(value = "/account/{number}")
    public Account account(@PathVariable(value = "number") Long number) {
        logger.info("get /account/{}", number);
        return accountService.find(number);
    }
}