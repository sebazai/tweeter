/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tweeter.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import tweeter.domain.Account;
import tweeter.domain.Comments;
import tweeter.domain.Likes;
import tweeter.domain.Messages;
import tweeter.repositories.CommentsRepository;
import tweeter.repositories.LikesRepository;
import tweeter.repositories.MessagesRepository;

/**
 *
 * @author sebserge
 */
@Service
public class MessagesService {

    @Autowired
    private MessagesRepository messageRepo;

    @Autowired
    private AccountService accountService;

    @Autowired
    private CommentsRepository commentsRepo;
    
    @Autowired
    private LikesRepository likesRepo;

    /**
     * Return correct model, also used for comment error checking. Which is kind of stupid, but needed to get it somehow working asap.
     * @param a Account, that is the authedAcc or owner of the message that gets the comment
     * @param auth Authed acc
     * @param notification If any, displayed on website
     * @param model To return to thymeleaf
     * @param owner Used so that we can get either your wall posts + your followers or the other guys post where you commented
     * @return model
     */
    public Model returnModel(Account a, Authentication auth, String notification, Model model, boolean owner) {
        model.addAttribute("owner", accountService.isOwner(auth, a));
        model.addAttribute("account", a);
        if (owner) {
            model.addAttribute("messages", accountService.getOwnersMessageStream(a));
        } else {
            model.addAttribute("messages", a.getMessages());
        }
        model.addAttribute("notification", notification);
        return model;
    }

    public void create(Account a, String tweeter) {
        Messages message = new Messages();
        message.setAccount(a);
        message.setMessage(tweeter);
        messageRepo.save(message);
    }

    public void addComment(String comment, Account authedAcc, Messages message) {
        Comments c = new Comments();
        c.setComment(comment);
        c.setAccount(authedAcc);
        c.setMessages(message);
        commentsRepo.save(c);
    }

    public void addLike(Account acc, Messages message) {
        if (likesRepo.findByAccountAndMessages(acc, message) != null) {
            return;
        }
        Likes l = new Likes();
        l.setAccount(acc);
        l.setMessages(message);
        likesRepo.save(l);
    }
    
}
