/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tweeter.controllers;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
    public String searchUsers(Model model) {
        model.addAttribute("users", accountRepo.findAll());
        return "search";
    }
    
    @PostMapping("/search")
    public String searchForUserWithString(Model model, @RequestParam String searchstring) {
        List<Account> foundUsers = accountRepo.findByNicknameContaining(searchstring);
        model.addAttribute("users", foundUsers);
        return "search";
    }
 }
