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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tweeter.domain.Account;
import tweeter.domain.Messages;
import tweeter.repositories.AccountRepository;
import tweeter.repositories.MessagesRepository;
import tweeter.services.AccountService;
import tweeter.services.MessagesService;

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
    
    @Autowired
    private MessagesService messagesService;
    
    @Autowired
    private AccountService accountService;
    
    /**
     * Post mapping for shout/tweet
     * @param model
     * @param auth
     * @param tweeter
     * @return 
     */
    
    @PostMapping("/shout")
    public String addNewShoutForUser(Model model, Authentication auth, @RequestParam String tweeter) {
        Account a = accountService.findAccountByUsername(auth.getName());
        if (tweeter.length() < 3 || tweeter.length() > 150) {
            model = messagesService.returnModel(a, auth, "Post should be between 3-100 chars.", model, true);
            model.addAttribute("isfollower", true);
            return "userprofile";
        }
        messagesService.create(a, tweeter);
        return "redirect:/users/" + a.getNickname();
    }
    
    /**
     * PostMap for commenting a post on your own wall or other peoples wall, checks if you are commenting your followers post on your own wall.
     * @param model
     * @param auth Authed user
     * @param postid the id of post that gets the comment
     * @param user Username of the wall where the comment was sent from
     * @param comment the comment string
     * @return 
     */
    @PostMapping("/comment/{user}/{postid}")
    public String commentPostId(Model model, Authentication auth, @PathVariable Long postid, @PathVariable String user, @RequestParam String comment) {
        Account authedAcc = accountRepo.findByUsername(auth.getName());
        Messages message = messageRepo.getOne(postid);
        Account ownerOfMessage = message.getAccount();
        
        String userRedirect;
        
        if (authedAcc.getUsername().equals(user)) {
            userRedirect = authedAcc.getNickname();
            model = messagesService.returnModel(authedAcc, auth, "", model, true);
        } else {
            userRedirect = ownerOfMessage.getNickname();
            model = messagesService.returnModel(ownerOfMessage, auth, "", model, false);
        }
        
        if (comment.length() < 3 || comment.length() > 100) {
            model.addAttribute("notification", "Comment length between 3-100 chars");
            model.addAttribute("isfollower", accountService.checkIfFollower(auth, ownerOfMessage.getNickname()));
            return "userprofile";
        }
        messagesService.addComment(comment, authedAcc, message);
        
        return "redirect:/users/" + userRedirect;
    }
    
    /**
     * Add a like to a post
     * @param auth Authed user
     * @param postid the message/post that gets the like
     * @param user Nickname of the wall where the like was sent from
     * @return 
     */
    @PostMapping("/like/{user}/{postid}")
    public String likePostId(Authentication auth, @PathVariable Long postid, @PathVariable String user) {
        Account acc = accountRepo.findByUsername(auth.getName());
        Messages message = messageRepo.getOne(postid);
        messagesService.addLike(acc, message);
        return "redirect:/users/" + user;
    }
}
