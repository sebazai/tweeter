/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tweeter.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author sebserge
 */
@Controller
public class AccountController {
    
    @GetMapping("/users/{nick}")
    @ResponseBody
    public String getUserProfile(Authentication auth, Model model, @PathVariable String nick) {
        if (auth.getName().equals(nick)) {
            return "jee";
        } else {
            return "nooo";
        }
    }
}
