/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tweeter.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tweeter.domain.Account;
import tweeter.domain.Followers;
import tweeter.repositories.AccountRepository;
import tweeter.repositories.FollowersRepository;

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
    
    @PostMapping("/follow")
    public String followUser(Authentication auth, @RequestParam Long idtofollow) {
        Account tofollow = accountRepo.getOne(idtofollow);
        Account follower = accountRepo.findByUsername(auth.getName());
        Followers newFollower = new Followers();
        newFollower.setAccount(tofollow);
        newFollower.setThefollower(follower);
        newFollower.setBlocked(false);
        followerRepo.save(newFollower);
        return "redirect:/search";
    }
}
