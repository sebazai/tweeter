/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tweeter.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tweeter.domain.Account;
import tweeter.repositories.AccountRepository;

/**
 *
 * @author sebserge
 */
@Controller
public class UserSearchController {
    @Autowired
    private AccountRepository accountRepo;
    
    @GetMapping("/search")
    public String searchUsers(Authentication auth, Model model) {
        String username = auth.getName();
        model.addAttribute("account", accountRepo.findByUsername(username));
        model.addAttribute("users", accountRepo.findAll());
        model.addAttribute("notification", "");
        return "search";
    }
    
    @PostMapping("/search")
    public String searchForUserWithString(Authentication auth, Model model, @RequestParam String searchstring) {
        String username = auth.getName();
        model.addAttribute("account", accountRepo.findByUsername(username));
        List<Account> foundUsers = accountRepo.findByNicknameContaining(searchstring);
        model.addAttribute("users", foundUsers);
        return "search";
    }
 }
