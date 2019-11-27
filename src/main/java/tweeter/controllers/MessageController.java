/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tweeter.controllers;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tweeter.domain.Account;
import tweeter.domain.Messages;
import tweeter.repositories.AccountRepository;
import tweeter.repositories.MessagesRepository;

/**
 *
 * @author sebserge
 */
@Controller
public class MessageController {
    
    @Autowired
    private MessagesRepository messageRepo;
    
    @Autowired
    private AccountRepository accountRepo;
    
    @PostMapping("/shout")
    public String addNewShoutForUser(Authentication auth, @RequestParam String tweeter) {
        Account a = accountRepo.findByUsername(auth.getName());
        Messages message = new Messages();
        message.setAccount(a);
        message.setMessage(tweeter);
        messageRepo.save(message);
        return "redirect:/";
    }
}
