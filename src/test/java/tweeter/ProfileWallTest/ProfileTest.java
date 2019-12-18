/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tweeter.ProfileWallTest;

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
public class ProfileTest {
    
    @Autowired
    private WebApplicationContext context;
    
    private MockMvc mockMvc;

    
    @Autowired
    private AccountRepository accountRep;
    
    @Autowired
    private MessagesRepository messagesRep;
    
    @Before
    public void setup() throws Exception {
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
    
    @Test
    @WithMockUser(username = "test", password = "tester123", roles = "USER")
    public void canSeeWallWhenLoggedIn() throws Exception {
        MvcResult res = mockMvc.perform(get("/users/tester")).andReturn();
        assertTrue(res.getResponse().getContentAsString().contains("No posts by tester"));
    }
    
    @WithMockUser("test")
    @Test
    public void anotherUsersPostNotShownOnOthersWallIfNotFollowing() throws Exception {
        registerAnotherUser();
        mockMvc.perform(post("/shout").param("tweeter", "hi there")).andReturn();
        MvcResult res = mockMvc.perform(get("/users/another")).andReturn();
        assertFalse(res.getResponse().getContentAsString().contains("hi there"));
    }
    
    @WithMockUser("test")
    @Test
    public void cantSeeCommentButtonIfNotFollowing() throws Exception {
        this.registerAnotherUser();
        this.addAPostForAnotherUser();
        MvcResult res = mockMvc.perform(get("/users/another")).andReturn();
        String content = res.getResponse().getContentAsString();
        assertFalse(content.contains("Click here to comment or like"));
        assertTrue(content.contains("hello there"));
    }
    
    @WithMockUser("test")
    @Test
    public void canSeeLikeButtonOfPosts() throws Exception {
        this.registerAnotherUser();
        this.addAPostForAnotherUser();
        MvcResult res = mockMvc.perform(get("/users/another")).andReturn();
        assertTrue(res.getResponse().getContentAsString().contains("Like"));
        assertTrue(res.getResponse().getContentAsString().contains("Likes: 0"));
    }
    
    @Test
    @WithMockUser("test")
    public void usersRedirectsToOwnProfile() throws Exception {
        mockMvc.perform(get("/users")).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/users/tester"));
    }
    
    @Test
    @WithMockUser("test")
    public void goToAProfileThatDoesntExistsRedirectsTo404NotFound() throws Exception {
        mockMvc.perform(get("/users/nakki")).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/404notfound"));
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
