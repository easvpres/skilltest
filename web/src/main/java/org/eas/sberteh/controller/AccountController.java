package org.eas.sberteh.controller;

import org.eas.sberteh.entity.Account;
import org.eas.sberteh.entity.AccountOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Arrays;

/**
 * @author aesipov.
 */
@Controller
public class AccountController {

    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Value("${services.host}")
    private String host;
    @Value("${services.port}")
    private String post;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/account")
    public ModelAndView accounts() {
        logger.info("account");
        URI uri = UriComponentsBuilder.newInstance().scheme("http").host(host).port(post).path("account").build().encode().toUri();
        ResponseEntity<Account[]> result = restTemplate.getForEntity(uri, Account[].class);
        return new ModelAndView("accounts", "accounts", Arrays.asList(result.getBody()));
    }

    @GetMapping("/account/{number}")
    public String account(@PathVariable String number, Model model) {
        logger.info("account/{}", number);

        URI uri1 = UriComponentsBuilder.newInstance().scheme("http").host(host).port(post).path("account/{number}").build().expand(number).encode().toUri();
        Account account = restTemplate.getForObject(uri1, Account.class);
        if (account == null) {
            model.addAttribute("number", number);
            return "accountnotfound";
        }

        URI uri2 = UriComponentsBuilder.newInstance().scheme("http").host(host).port(post).path("operation").queryParam("accountNumber", number).build().expand(number).encode().toUri();
        ResponseEntity<AccountOperation[]> result = restTemplate.getForEntity(uri2, AccountOperation[].class);
        model.addAttribute("account", account);
        model.addAttribute("operations", Arrays.asList(result.getBody()));
        return "account";
    }

    @PostMapping("/findaccount")
    public ModelAndView findAccount(@RequestParam Long number) {
        return new ModelAndView("redirect:/account/{number}", "number", number);
    }
}