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
import tweeter.domain.Followers;
import tweeter.repositories.FollowersRepository;
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
    private FollowersRepository followerRepo;
    
    @GetMapping("/users/{nick}")
    public String getUserProfile(Authentication auth, Model model, @PathVariable String nick) {
        Account a = accountService.findAccount(nick);
        
        if (a == null) {
            return "redirect:/404notfound";
        }

        model.addAttribute("owner", accountService.isOwner(auth, a));
        model.addAttribute("account", a);
        model.addAttribute("messages", a.getMessages());
        return "userprofile";
    }
    
    @GetMapping("/users/{nick}/followers")
    public String showUserFollowers(Model model, @PathVariable String nick) {
        Account a = accountService.findAccount(nick);
        if (a == null) {
            return "redirect:/404notfound";
        }
        List<Followers> followers = a.getFollowers();
        model.addAttribute("account", a);
        model.addAttribute("follow", followers);
        model.addAttribute("followers", true);
        return "follows";
    }
    
    @GetMapping("/users/{nick}/following")
    public String showWhoUserIsFollowing(Model model, @PathVariable String nick) {
        Account a = accountService.findAccount(nick);
        if (a == null) {
            return "redirect:/404notfound";
        }
        List<Followers> following = followerRepo.findByThefollowerId(a.getId());
        model.addAttribute("account", a);
        model.addAttribute("follow", following);
        model.addAttribute("followers", false);
        return "follows";
    }
    

}

