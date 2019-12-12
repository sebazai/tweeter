/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tweeter.controllers;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import tweeter.domain.Account;
import tweeter.domain.Comments;
import tweeter.domain.Likes;
import tweeter.domain.Photos;
import tweeter.repositories.AccountRepository;
import tweeter.repositories.CommentsRepository;
import tweeter.repositories.LikesRepository;
import tweeter.repositories.PhotosRepository;
import tweeter.services.AccountService;

/**
 *
 * @author sebserge
 */
@Controller
public class PhotosController {
    
    @Autowired
    private AccountRepository accountRepo;
    
    @Autowired
    private AccountService accountService;
    
    @Autowired
    private PhotosRepository photosRepo;
    
    @Autowired
    private CommentsRepository commentsRepo;
    
    @Autowired
    private LikesRepository likesRepo;
    
    @GetMapping("/users/{user}/album")
    public String getUserAlbum(Authentication auth, Model model, @PathVariable String user) {
        Account a = accountRepo.findByUsername(user);
        model.addAttribute("account", a);
        model.addAttribute("owner", accountService.isOwner(auth, a));
        return "album";
    }

    @GetMapping(path="/users/img/{id}", produces = "image/*")
    @ResponseBody
    public byte[] get(@PathVariable Long id) {
        return photosRepo.getOne(id).getPhoto();
    }
    
    @PostMapping("/users/{user}/img")
    public String addImage(Authentication auth, @PathVariable String user, Model model, @RequestParam String description, @RequestParam("file") MultipartFile file) throws IOException {
        Account a = accountRepo.findByUsername(user);
        model.addAttribute("account", a);
        model.addAttribute("owner", accountService.isOwner(auth, a));  
        if (a.getPhotos().size() == 10) {
            model.addAttribute("notification", "Only 10 photos allowed, you have reached your limit.");
            return "album";
        }
        Photos photo = new Photos();
        photo.setAccount(a);
        photo.setPhotoText(description);
        photo.setPhoto(file.getBytes());
        photosRepo.save(photo);
        return "redirect:/users/{user}/album";
    }
    
    @PostMapping("/users/{username}/profilepic/{picid}")
    public String setProfilePic(@PathVariable String username, @PathVariable Long picid) {
        Account a = accountRepo.findByNickname(username);
        a.setProfilePicId(picid);
        accountRepo.save(a);
        return "redirect:/users/{username}/album";
    }
    
    @PostMapping("/comment/img/{imgid}")
    public String commentImgId(Authentication auth, @PathVariable Long imgid, @RequestParam String comment) {
        Account acc = accountRepo.findByUsername(auth.getName());
        Photos photo = photosRepo.getOne(imgid);
        String user = photo.getAccount().getNickname();
        Comments c = new Comments();
        c.setComment(comment);
        c.setAccount(acc);
        c.setPhotos(photo);
        commentsRepo.save(c);
        return "redirect:/users/" + user + "/album";
    }
    
    @PostMapping("/like/img/{imgid}")
    public String likeImgId(Authentication auth, @PathVariable Long imgid) {
        Account acc = accountRepo.findByUsername(auth.getName());
        Photos photo = photosRepo.getOne(imgid);
        String user = photo.getAccount().getNickname();
        if (likesRepo.findByAccountAndPhotos(acc, photo) != null) {
            return "redirect:/users/" + user + "/album";
        }
        Likes l = new Likes();
        l.setAccount(acc);
        l.setPhotos(photo);
        likesRepo.save(l);
        return "redirect:/users/" + user + "/album";
    }
    
}
