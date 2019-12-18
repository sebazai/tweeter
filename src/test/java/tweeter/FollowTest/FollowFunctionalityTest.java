/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tweeter.FollowTest;

import org.junit.After;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
public class FollowFunctionalityTest {
    
    @Autowired
    private WebApplicationContext context;
    
    private MockMvc mockMvc;

    
    @Autowired
    private AccountRepository accountRep;
    
    @Autowired
    private MessagesRepository messagesRep;
    
    @Autowired
    private FollowersRepository followerRep;
    
    @Before
    public void setup() throws Exception {
        followerRep.deleteAllInBatch();
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
    }
    
    @After
    public void end() throws Exception {
        followerRep.deleteAllInBatch();
    }
    
    @Test
    @WithMockUser("test")
    public void canFollowUser() throws Exception {
        registerAnotherUser();
        Account follow = accountRep.findByNickname("another");
        mockMvc.perform(post("/follow").param("idtofollow", follow.getId().toString()));
        MvcResult res = mockMvc.perform(get("/users/tester/following")).andReturn();
        String content = res.getResponse().getContentAsString();
        assertTrue(content.contains("another"));
    }
    
    @Test
    @WithMockUser("test")
    public void cantFollowTwiceSameUser() throws Exception {
        registerAnotherUser();
        Account follow = accountRep.findByNickname("another");
        mockMvc.perform(post("/follow").param("idtofollow", follow.getId().toString()));
        MvcResult res = mockMvc.perform(post("/follow").param("idtofollow", follow.getId().toString())).andReturn();
        assertTrue(res.getResponse().getContentAsString().contains("You are already following another"));
    }
    
    @Test
    @WithMockUser("test")
    public void followersAreShown() throws Exception {
        registerAnotherUser();
        Account follow = accountRep.findByNickname("another");
        mockMvc.perform(post("/follow").param("idtofollow", follow.getId().toString()));
        
        MvcResult res = mockMvc.perform(get("/users/another/followers")).andReturn();
        assertTrue(res.getResponse().getContentAsString().contains("User tester started following you on"));
    }
    
    @Test
    @WithMockUser("test")
    public void followingAreShown() throws Exception {
        registerAnotherUser();
        Account following = accountRep.findByNickname("another");
        mockMvc.perform(post("/follow").param("idtofollow", following.getId().toString()));
        
        MvcResult res = mockMvc.perform(get("/users/tester/following")).andReturn();
        assertTrue(res.getResponse().getContentAsString().contains("Started following another on"));
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
