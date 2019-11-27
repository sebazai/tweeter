/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tweeter.controllers;

import java.util.HashSet;
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
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;
    
    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register";
    }
    
    @PostMapping("/register")
    public String registerUser(Model model, @RequestParam String username, 
            @RequestParam String password, 
            @RequestParam String passwordConfirm, 
            @RequestParam String nickname) {
        Account a = new Account();
        System.out.println(username);
        a.setUsername(username);
        a.setPassword(passwordEncoder.encode(password));
        a.setNickname(nickname);
        accountRepository.save(a);
        return "redirect:/login";
    }
}
