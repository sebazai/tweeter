/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tweeter.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import tweeter.domain.Account;
import tweeter.repositories.AccountRepository;
import tweeter.repositories.MessagesRepository;

/**
 *
 * @author sebserge
 */
@Controller
public class AccountController {
    
    @Autowired
    private AccountRepository accountRepo;
    
    @Autowired
    private MessagesRepository messageRepo;
    
    @GetMapping("/users/{nick}")
    public String getUserProfile(Authentication auth, Model model, @PathVariable String nick) {
        Account a = accountRepo.findByNickname(nick);
        if (a == null) {
            return "redirect:/404notfound";
        }
        
        if (auth.getName().equals(a.getUsername())) {
            model.addAttribute("owner", true);
        }
        
        model.addAttribute("account", a);
        return "userprofile";
    }
}

