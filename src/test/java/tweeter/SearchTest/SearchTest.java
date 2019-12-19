/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tweeter.SearchTest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.junit.After;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tweeter.domain.Account;
import tweeter.domain.Messages;
import tweeter.repositories.AccountRepository;
import tweeter.repositories.CommentsRepository;
import tweeter.repositories.FollowersRepository;
import tweeter.repositories.MessagesRepository;
/**
 * Test some of the functionality of the website, how the user walls should work.
 * 
 * @author sebserge
 */
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@WebAppConfiguration
@SpringBootTest
public class SearchTest {
    
    @Autowired
    private WebApplicationContext context;
    
    private MockMvc mockMvc;

    
    @Autowired
    private AccountRepository accountRep;
    
    @Autowired
    private MessagesRepository messagesRep;
    
    @Autowired
    private FollowersRepository followerRep;
    
    @Autowired
    private CommentsRepository commentsRep;
    
    @Before
    public void setup() throws Exception {
        followerRep.deleteAllInBatch();
        commentsRep.deleteAllInBatch();
        messagesRep.deleteAllInBatch();
        accountRep.deleteAllInBatch();
        mockMvc = MockMvcBuilders
          .webAppContextSetup(context)
          .apply(SecurityMockMvcConfigurers.springSecurity())
          .build();
        mockMvc.perform(post("/register")
            .param("username", "test")
            .param("password", "tester123")
            .param("passwordConfirm", "tester123")
            .param("nickname", "tester"))
        .andReturn();
        registerAnotherUser();
    }
    
    @WithMockUser("test")
    @Test
    public void searchShowsUsers() throws Exception {
        MvcResult res = mockMvc.perform(get("/search")).andReturn();
        String contains = res.getResponse().getContentAsString();
        assertTrue(contains.contains("Search for users"));
        assertTrue(contains.contains("another"));
        assertTrue(contains.contains("tester"));
    }
    
    @WithMockUser("test")
    @Test
    public void searchUserWorks() throws Exception {
        MvcResult res = mockMvc.perform(post("/search").param("searchstring", "other")).andReturn();
        assertTrue(res.getResponse().getContentAsString().contains("another"));
    }
    
    @WithMockUser("test")
    @Test
    public void searchUserNotFound() throws Exception {
        MvcResult res = mockMvc.perform(post("/search").param("searchstring", "asd")).andReturn();
        assertTrue(res.getResponse().getContentAsString().contains("No users found"));
    }
    
    public void registerAnotherUser() throws Exception {
        mockMvc.perform(post("/register")
            .param("username", "another")
            .param("password", "tester123")
            .param("passwordConfirm", "tester123")
            .param("nickname", "another"))
        .andReturn();
    }
    
    public void addAPostForAnotherUser() {
        Account another = accountRep.findByNickname("another");
        Messages message = new Messages();
        message.setAccount(another);
        message.setMessage("Well hello there");
        messagesRep.save(message);
    }
    

}
