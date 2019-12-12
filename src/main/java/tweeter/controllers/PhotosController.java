/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tweeter.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 *
 * @author sebserge
 */
@Controller
public class PhotosController {
    @GetMapping("/users/{user}/album")
    public String getUserAlbum(@PathVariable String user) {
        return null;
        
    }
}
