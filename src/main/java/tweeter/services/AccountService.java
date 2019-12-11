/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tweeter.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import tweeter.domain.Account;
import tweeter.repositories.AccountRepository;

/**
 *
 * @author sebserge
 */
@Service
public class AccountService {
    
    @Autowired
    private AccountRepository accountRepo;
    
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
}
