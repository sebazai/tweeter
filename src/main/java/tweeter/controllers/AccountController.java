/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tweeter.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import tweeter.domain.Account;
import tweeter.domain.Followers;
import tweeter.repositories.AccountRepository;
import tweeter.repositories.FollowersRepository;
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
    
    @Autowired
    private FollowersRepository followersrepo;
    
    @GetMapping("/users/{nick}")
    public String getUserProfile(Authentication auth, Model model, @PathVariable String nick) {
        Account a = findAccount(nick);
        if (a == null) {
            return "redirect:/404notfound";
        }
        boolean owner = isOwner(auth, a);
        model.addAttribute("owner", owner);
        model.addAttribute("account", a);
        model.addAttribute("messages", a.getMessages());
        return "userprofile";
    }
    
    @GetMapping("/users/{nick}/followers")
    public String showUserFollowers(Authentication auth, Model model, @PathVariable String nick) {
        Account a = findAccount(nick);
        if (a == null) {
            return "redirect:/404notfound";
        }
        List<Followers> fol = a.getFollowers();
        model.addAttribute("account", a);
        model.addAttribute("follow", fol);
        return "follows";
    }
    @GetMapping("/users/{nick}/following")
    public String showWhoUserIsFollowing(Model model) {
        return "follows";
    }
    
    private Account findAccount(String nick) {
        Account a = accountRepo.findByNickname(nick);
        if (a == null) {
            return null;
        }
        return a;
    }
    
    private boolean isOwner(Authentication auth, Account a) {
        if (auth.getName().equals(a.getUsername())) {
            return true;
        }
        return false;
    }
}

