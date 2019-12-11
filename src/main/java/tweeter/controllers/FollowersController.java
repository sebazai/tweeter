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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tweeter.domain.Account;
import tweeter.domain.Followers;
import tweeter.repositories.AccountRepository;
import tweeter.repositories.FollowersRepository;
import tweeter.services.AccountService;

/**
 *
 * @author sebserge
 */
@Controller
public class FollowersController {
    @Autowired
    private FollowersRepository followerRepo;
    
    @Autowired
    private AccountRepository accountRepo;
    
    @Autowired
    private AccountService accountService;
    
    @PostMapping("/follow")
    public String followUser(Authentication auth, @RequestParam Long idtofollow, Model model) {
        Account tofollow = accountRepo.getOne(idtofollow);
        Account follower = accountRepo.findByUsername(auth.getName());
        model.addAttribute("account", follower);
        model.addAttribute("users", accountRepo.findAll());
        if (followerRepo.findByAccountIdAndThefollowerId(idtofollow, follower.getId()) != null) {
            String follownoti = "You are already following " + tofollow.getNickname() + "";
            model.addAttribute("notification", follownoti);
            return "search";
        }
        String follownoti = "Started following " + tofollow.getNickname() + "";
        model.addAttribute("notification", follownoti);
        Followers newFollower = new Followers();
        newFollower.setAccount(tofollow);
        newFollower.setThefollower(follower);
        newFollower.setBlocked(false);
        followerRepo.save(newFollower);
        return "search";
    }
    
    @GetMapping("/users/{nick}/followers")
    public String showUserFollowers(Authentication auth, Model model, @PathVariable String nick) {
        Account a = accountService.findAccount(nick);
        if (a == null) {
            return "redirect:/404notfound";
        }
        List<Followers> followers = a.getFollowers();
        model.addAttribute("owner", accountService.isOwner(auth, a));
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
