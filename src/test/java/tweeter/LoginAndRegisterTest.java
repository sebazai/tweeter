/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tweeter;

import javax.transaction.Transactional;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.FormLoginRequestBuilder;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.web.context.WebApplicationContext;
import tweeter.repositories.AccountRepository;
import tweeter.repositories.MessagesRepository;
/**
 *
 * @author sebserge
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class LoginAndRegisterTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private AccountRepository accountRep;
    
    @Autowired
    private MessagesRepository messagesRep;
    
    @Before
    public void setup() throws Exception {
        messagesRep.deleteAllInBatch();
        accountRep.deleteAllInBatch();
        mockMvc.perform(post("/register")
            .param("username", "test")
            .param("password", "tester123")
            .param("passwordConfirm", "tester123")
            .param("nickname", "tester"))
        .andReturn();
    }
    
    @Test
    public void canCreateAccount() throws Exception {
        accountRep.deleteAll();
        mockMvc.perform(post("/register").param("username", "testing")
            .param("password", "tester123")
            .param("passwordConfirm", "tester123")
            .param("nickname", "testering")).andReturn();
        assertTrue(accountRep.findByNickname("testering") != null);
    }
    
    @Test
    public void cantLoginWithFakeAccount() throws Exception {
        FormLoginRequestBuilder login = formLogin().user("notfound").password("hehe");
        mockMvc.perform(login).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/login?error"));
    }
    
    @Test
    public void cantGoToAnySiteWithoutLoggingIn() throws Exception {
        MvcResult res = mockMvc.perform(get("/users/tester")).andExpect(status().is3xxRedirection()).andReturn();
        assertTrue(res.getResponse().getRedirectedUrl().contains("/login"));
    }
    
    @Test
    public void canLoginWithUser() throws Exception {
        FormLoginRequestBuilder login = formLogin().user("test").password("tester123");
        mockMvc.perform(login).andExpect(authenticated().withUsername("test")).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/successfullogin"));
    }
    
    @Test
    @Transactional
    @WithMockUser(username = "test", password = "tester123", roles = "USER")
    public void canSeeWallWhenLoggedIn() throws Exception {
        MvcResult res = mockMvc.perform(get("/users/tester")).andReturn();
        assertTrue(res.getResponse().getContentAsString().contains("No posts by tester"));
    }
}
