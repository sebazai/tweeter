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
    
    public Account findAccount(String nick) {
    Account a = accountRepo.findByNickname(nick);
        if (a == null) {
            return null;
        }
        return a;
    }
    
    public boolean isOwner(Authentication auth, Account a) {
        return auth.getName().equals(a.getUsername());
    }

    public List<Messages> getOwnersMessageStream(Account a) {
        List<Followers> following = followersRepo.findByThefollowerId(a.getId());
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
