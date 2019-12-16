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
import org.springframework.web.bind.annotation.PathVariable;
import tweeter.domain.Account;
import tweeter.domain.Messages;
import tweeter.repositories.AccountRepository;
import tweeter.services.AccountService;

/**
 *
 * @author sebserge
 */
@Controller
public class AccountController {
    
    @Autowired
    private AccountService accountService;
    
    @Autowired
    private AccountRepository accountRepo;
    
    @GetMapping("/users")
    public String redirectToOwnWall(Authentication auth, Model model) {
        Account a = accountRepo.findByUsername(auth.getName());
        return "redirect:/users/" + a.getNickname();
    }
    
    @GetMapping("/users/{nick}")
    public String getUserProfile(Authentication auth, Model model, @PathVariable String nick) {
        Account a = accountService.findAccount(nick);
        Boolean owner = accountService.isOwner(auth, a);
        if (a == null) {
            return "redirect:/404notfound";
        }
        if (owner) {
            List<Messages> yoursAndFollowingMessages = accountService.getOwnersMessageStream(a);
            model.addAttribute("messages", yoursAndFollowingMessages);
        } else {
            model.addAttribute("messages", a.getMessages());
        }
        
        model.addAttribute("isfollower", accountService.checkIfFollower(auth, nick));
        model.addAttribute("owner", owner);
        model.addAttribute("account", a);
        model.addAttribute("notification", "");
        return "userprofile";
    }

}

