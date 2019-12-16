/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tweeter.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import tweeter.domain.Account;
import tweeter.domain.Followers;
import tweeter.repositories.FollowersRepository;

/**
 *
 * @author sebserge
 */
@Service
public class FollowersService {
    @Autowired
    private AccountService accountService;
    
    @Autowired
    private FollowersRepository followerRepo;
    
    /**
     * Returns the model for thymeleaf with correct objects
     * @param model
     * @param showFollowers Show either followers or the following people
     * @param auth Authed used
     * @param a Account whos followers/following people you watching
     * @param notification If any, displayed on website
     * @return 
     */
    public Model returnFollowModel(Model model, Boolean showFollowers, Authentication auth, Account a, String notification) {
        List<Followers> followers;
        if (showFollowers) {
            followers = a.getFollowers();
        } else {
            followers = followerRepo.findByThefollowerId(a.getId());
        }
        model.addAttribute("owner", accountService.isOwner(auth, a));
        model.addAttribute("account", a);
        model.addAttribute("follow", followers);
        model.addAttribute("followers", showFollowers);
        model.addAttribute("notification", notification);
        return model;
    }
    
    @Transactional
    public List<Followers> findByThefollowerId(Long id) {
        return followerRepo.findByThefollowerId(id);
    }
}
