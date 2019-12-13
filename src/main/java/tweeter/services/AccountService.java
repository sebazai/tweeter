/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tweeter.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tweeter.domain.Account;
import tweeter.domain.Followers;
import tweeter.domain.Messages;
import tweeter.repositories.AccountRepository;
import tweeter.repositories.FollowersRepository;

/**
 *
 * @author sebserge
 */
@Service
public class AccountService {
    
    @Autowired
    private AccountRepository accountRepo;
    
    @Autowired
    private FollowersRepository followersRepo;
    
    @Autowired
    private FollowersService followersService;
    
    @Transactional
    public Account findAccount(String nick) {
    Account a = accountRepo.findByNickname(nick);
        if (a == null) {
            return null;
        }
        return a;
    }
    
    @Transactional
    public Account findAccountByUsername(String username) {
        Account a = accountRepo.findByUsername(username);
        return a;
    }
    
    @Transactional
    public boolean userExists(String username) {
        return (accountRepo.findByUsername(username) != null);
    }
    
    @Transactional
    public void save(Account a) {
        accountRepo.save(a);
    }
    
    @Transactional
    public boolean nicknameExists(String nickname) {
        return (accountRepo.findByNickname(nickname) != null);
    }
    
    /**
     * Checks if the authenticated user and the pages account where you were is your own or someone elses
     * @param auth
     * @param a
     * @return 
     */
    public boolean isOwner(Authentication auth, Account a) {
        return auth.getName().equals(a.getUsername());
    }

    /**
     * Get all followers for account, remove all followers that has blocked you, get all the account and get all the messages for these account, sort them by date and return.
     * @param a Accounts messages + followers messages to get.
     * @return Latest 25 posts by you and your followers ordered by post date.
     */
    @Transactional
    public List<Messages> getOwnersMessageStream(Account a) {
        List<Followers> following = followersService.findByThefollowerId(a.getId());
        following.removeIf(fol -> fol.isBlocked() == true);
        List<Account> accounts = following.stream().map(f -> f.getAccount()).collect(Collectors.toList());
        List<Messages> messages = accounts.stream().flatMap(acc -> acc.getMessages().stream()).collect(Collectors.toList());
        messages.addAll(a.getMessages());
        Collections.sort(messages, Collections.reverseOrder());
        if (messages.size() > 25) {
            return messages.subList(0, 25);
        } else {
            return messages;
        }
    }
}
