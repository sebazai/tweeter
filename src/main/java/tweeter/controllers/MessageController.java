/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tweeter.controllers;

import java.time.LocalDateTime;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tweeter.domain.Account;
import tweeter.domain.Comments;
import tweeter.domain.Likes;
import tweeter.domain.Messages;
import tweeter.repositories.AccountRepository;
import tweeter.repositories.CommentsRepository;
import tweeter.repositories.LikesRepository;
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
    
    @PostMapping("/shout")
    public String addNewShoutForUser(Model model, Authentication auth, @RequestParam String tweeter) {
        Account a = accountRepo.findByUsername(auth.getName());
        if (tweeter.length() < 3 || tweeter.length() > 150) {
            model = messagesService.returnModel(a, auth, "Post should be between 3-100 chars.", model, true);
            return "userprofile";
        }
        messagesService.create(a, tweeter);
        return "redirect:/users/" + a.getNickname();
    }
    
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
            return "userprofile";
        }
        messagesService.addComment(comment, authedAcc, message);
        
        return "redirect:/users/" + userRedirect;
    }
    
    @PostMapping("/like/{user}/{postid}")
    public String likePostId(Authentication auth, @PathVariable Long postid, @PathVariable String user) {
        Account acc = accountRepo.findByUsername(auth.getName());
        Messages message = messageRepo.getOne(postid);
        messagesService.addLike(acc, message);
        return "redirect:/users/" + user;
    }
}
