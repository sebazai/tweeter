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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import tweeter.domain.Account;
import tweeter.services.AccountService;

/**
 *
 * @author sebserge
 */
@Controller
public class AccountRegisterController {
    
    @Autowired
    private AccountService accountService;

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
        if (accountService.nicknameExists(account.getNickname())) {
            bindingResult.rejectValue("nickname", "error.account", "Nickname already taken.");
        }
        if (accountService.userExists(account.getUsername())) {
            bindingResult.rejectValue("username", "error.account", "Username already taken.");
        }
        if (bindingResult.hasErrors()) {
            return "register";
        }
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        account.setPasswordConfirm("ok");
        accountService.save(account);
        return "redirect:/login";
    }
}
