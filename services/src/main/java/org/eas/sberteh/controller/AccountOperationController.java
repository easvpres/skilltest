package org.eas.sberteh.controller;

import org.eas.sberteh.entity.AccountOperation;
import org.eas.sberteh.service.AccountOperationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author aesipov.
 */
@RestController
public class AccountOperationController {

    private static final Logger logger = LoggerFactory.getLogger(AccountOperationController.class);

    @Autowired
    private AccountOperationService accountOperationService;

    @GetMapping(value = "/operation")
    public List<AccountOperation> operation(@RequestParam(name = "accountNumber", required = false) Long accountNumber) {
        logger.info("get /operation?accountNumber={}", accountNumber);
        if (accountNumber != null) {
            return accountOperationService.findOperationByNumber(accountNumber);
        } else {
            return accountOperationService.findAllOperations();
        }
    }

    @PostMapping(value = "/operation", consumes = "application/json")
    public void register(@RequestBody AccountOperation accountOperation) {
        logger.info("post /operation: accountOperation={}", accountOperation);
        accountOperationService.register(accountOperation);
    }
}