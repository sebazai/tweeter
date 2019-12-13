/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tweeter.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tweeter.domain.Account;
import tweeter.domain.Followers;
import tweeter.repositories.AccountRepository;
import tweeter.repositories.FollowersRepository;
import tweeter.services.AccountService;
import tweeter.services.FollowersService;

/**
 *
 * @author sebserge
 */
@Controller
public class FollowersController {
    @Autowired
    private FollowersRepository followerRepo;
    
    @Autowired
    private FollowersService followerService;
    
    @Autowired
    private AccountRepository accountRepo;
    
    @Autowired
    private AccountService accountService;
    
    /**
     * Checks if you are following the dude, if not, adds you as his follower.
     * @param auth Authenticated user
     * @param idtofollow
     * @param model
     * @return 
     */
    @PostMapping("/follow")
    public String followUser(Authentication auth, @RequestParam Long idtofollow, Model model) {
        Account tofollow = accountRepo.getOne(idtofollow);
        Account follower = accountRepo.findByUsername(auth.getName());
        model.addAttribute("account", follower);
        model.addAttribute("users", accountRepo.findAll());
        Followers findIfFollowing = followerRepo.findByAccountIdAndThefollowerId(idtofollow, follower.getId());
        if (findIfFollowing != null) {
            String follownoti = "You are already following " + tofollow.getNickname() + "";
            if (findIfFollowing.isBlocked()) {
                follownoti = "That user has blocked you for some reason. :(";
            }
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
    
    /**
     * Return accounts followers
     * @param auth
     * @param model
     * @param nick the users followers to return
     * @return 
     */
    @GetMapping("/users/{nick}/followers")
    public String showUserFollowers(Authentication auth, Model model, @PathVariable String nick) {
        Account a = accountService.findAccount(nick);
        if (a == null) {
            return "redirect:/404notfound";
        }
        followerService.returnFollowModel(model, true, auth, a, "");
        return "follows";
    }
    
    /**
     * Returns who is following this account
     * @param auth
     * @param model
     * @param nick the user who everyone is following
     * @return 
     */
    @GetMapping("/users/{nick}/following")
    public String showWhoUserIsFollowing(Authentication auth, Model model, @PathVariable String nick) {
        Account a = accountService.findAccount(nick);
        if (a == null) {
            return "redirect:/404notfound";
        }
        followerService.returnFollowModel(model, false, auth, a, "");
        return "follows";
    }
    
    /**
     * Block user
     * @param model
     * @param auth
     * @param idtoblock
     * @return 
     */
    @Transactional
    @PostMapping("/follower/block/{idtoblock}")
    public String removeAndBlockFollower(Model model, Authentication auth, @PathVariable Long idtoblock) {
        Account loggedinacc = accountRepo.findByUsername(auth.getName());
        Followers follower = followerRepo.findByAccountIdAndThefollowerId(loggedinacc.getId(), idtoblock);
        follower.setBlocked(true);
        followerService.returnFollowModel(model, true, auth, loggedinacc, "Removed and blocked user " + follower.getThefollower().getNickname() + "");
        return "follows";
    }
    
    /**
     * Unfollow someone you are following.
     * @param model
     * @param auth
     * @param id
     * @return 
     */
    @PostMapping("/follower/unfollow/{id}")
    public String unfollowUser(Model model, Authentication auth, @PathVariable Long id) {
        Account loggedinacc = accountRepo.findByUsername(auth.getName());
        Followers follower = followerRepo.findByAccountIdAndThefollowerId(id, loggedinacc.getId());
        String noti = "Unfollowed user " + follower.getThefollower().getNickname() + "";
        followerRepo.delete(follower);
        followerService.returnFollowModel(model, false, auth, loggedinacc, noti);
        return "follows";
    }
}
