package org.eas.sberteh.controller;

import org.eas.sberteh.entity.AccountOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Arrays;

/**
 * @author aesipov.
 */
@Controller
public class OperationController {

    private static final Logger logger = LoggerFactory.getLogger(OperationController.class);

    @Value("${services.host}")
    private String host;
    @Value("${services.port}")
    private String post;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/operation")
    public ModelAndView operation() {
        logger.info("operation");
        URI uri = UriComponentsBuilder.newInstance().scheme("http").host(host).port(post).path("operation").build().encode().toUri();
        ResponseEntity<AccountOperation[]> result = restTemplate.getForEntity(uri, AccountOperation[].class);
        return new ModelAndView("operations", "operations", Arrays.asList(result.getBody()));
    }

    @GetMapping("/newoperation")
    public ModelAndView newoperationForm(@RequestParam(name = "from", required = false) Long from) {
        logger.info("newoperation: from={}", from);
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setFromBankAccountNumber(from);
        return new ModelAndView("newoperation", "operation", accountOperation);
    }

    @PostMapping(value = "/newoperation", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ModelAndView newoperation(@Valid AccountOperation accountOperation, BindingResult result) {
        logger.info("newoperation: accountOperation={}", accountOperation);
        if (result.hasErrors()) {
            return new ModelAndView("messages/form", "formErrors", result.getAllErrors());
        }
        URI uri = UriComponentsBuilder.newInstance().scheme("http").host(host).port(post).path("operation").build().encode().toUri();
        ResponseEntity<Object> objectResponseEntity = restTemplate.postForEntity(uri, accountOperation, Object.class);
//        objectResponseEntity.getStatusCode() == HttpStatus.OK
        return new ModelAndView("redirect:/account/{number}", "number", accountOperation.getFromBankAccountNumber());
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }
}