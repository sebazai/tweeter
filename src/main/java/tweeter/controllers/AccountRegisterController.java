/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tweeter.controllers;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tweeter.domain.Account;
import tweeter.repositories.AccountRepository;

/**
 *
 * @author sebserge
 */
@Controller
public class AccountRegisterController {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @GetMapping("/register")
    public String showRegistrationForm(@ModelAttribute Account account) {
        return "register";
    } 
    
    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute Account account, BindingResult bindingResult) {
        if (!account.getPassword().equals(account.getPasswordConfirm())) {
            bindingResult.rejectValue("passwordConfirm", "error.account", "Passwords do not match");
        }
        if (account.getPassword().length() > 20) {
            bindingResult.rejectValue("password", "error.account", "Password too long, maximum 20 characters");
        }
        if (bindingResult.hasErrors()) {
            return "register";
        }
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        account.setPasswordConfirm("ok");
        accountRepository.save(account);
        return "redirect:/login";
    }
}
