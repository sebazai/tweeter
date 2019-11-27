package tweeter.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tweeter.domain.Account;
import tweeter.repositories.AccountRepository;

@Controller
public class DefaultController {
    
    @Autowired
    private AccountRepository accountRepo;
    
    @GetMapping("*")
    @ResponseBody
    public String FourOFourNotFound() {
        return "<h1>404 Not Found</h1>";
    }
    
    @GetMapping("/")
    public String registerOrLogIn(Model model) {
        return "redirect:/first";
    }
    
    @GetMapping("/first")
    public String toRegisterOrLoginChoosing() {
        return "registerorlogin";
    }
    
    @GetMapping("/successfulogin")
    public String redirectSuccessfulLoginToOwnProfile(Authentication auth) {
        Account a = accountRepo.findByUsername(auth.getName());
        return "redirect:/users/" + a.getNickname();
    }
}
