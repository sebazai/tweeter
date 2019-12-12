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

/**
 *
 * @author sebserge
 */
@Controller
public class MessageController {
    
    @Autowired
    private MessagesRepository messageRepo;
    
    @Autowired
    private CommentsRepository commentsRepo;
    
    @Autowired
    private AccountRepository accountRepo;
    
    @Autowired
    private LikesRepository likesRepo;
    
    @PostMapping("/shout")
    public String addNewShoutForUser(Authentication auth, @RequestParam String tweeter) {
        Account a = accountRepo.findByUsername(auth.getName());
        Messages message = new Messages();
        message.setAccount(a);
        message.setMessage(tweeter);
        messageRepo.save(message);
        String nick = a.getNickname();
        return "redirect:/users/" + nick;
    }
    
    @PostMapping("/comment/{postid}")
    public String commentPostId(Authentication auth, @PathVariable Long postid, @RequestParam String comment) {
        Account acc = accountRepo.findByUsername(auth.getName());
        Messages message = messageRepo.getOne(postid);
        String user = message.getAccount().getNickname();
        Comments c = new Comments();
        c.setComment(comment);
        c.setAccount(acc);
        c.setMessages(message);
        commentsRepo.save(c);
        return "redirect:/users/" + user + "#post" + postid;
    }
    
    @PostMapping("/like/{postid}")
    public String likePostId(Authentication auth, @PathVariable Long postid) {
        Account acc = accountRepo.findByUsername(auth.getName());
        Messages message = messageRepo.getOne(postid);
        String user = message.getAccount().getNickname();
        if (likesRepo.findByAccountAndMessages(acc, message) != null) {
            return "redirect:/users/" + user;
        }
        Likes l = new Likes();
        l.setAccount(acc);
        l.setMessages(message);
        likesRepo.save(l);
        return "redirect:/users/" + user;
    }
}
