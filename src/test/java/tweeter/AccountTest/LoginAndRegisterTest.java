/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tweeter.AccountTest;

import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.FormLoginRequestBuilder;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
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
import tweeter.repositories.AccountRepository;
import tweeter.repositories.MessagesRepository;
/**
 *
 * @author sebserge
 */
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@WebAppConfiguration
@SpringBootTest
public class LoginAndRegisterTest {
    
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
    public void redirectsToRegisterAndLoginIfNotLoggedIn() throws Exception {
        mockMvc.perform(get("/")).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/first")).andReturn();
    }
    
    @Test
    public void returnLoginAndRegisterScreen() throws Exception {
        MvcResult res = mockMvc.perform(get("/first")).andReturn();
        String content = res.getResponse().getContentAsString();
        assertTrue(content.contains("Hello Stranger"));
    }
    
    @Test
    @WithMockUser("test")
    public void f404notfound() throws Exception {
        MvcResult res = mockMvc.perform(get("/somehwere")).andReturn();
        assertTrue(res.getResponse().getContentAsString().contains("404 Not Found"));
    }
    
    @Test
    @WithMockUser("test")
    public void successfullloginRedirects() throws Exception {
        mockMvc.perform(get("/successfullogin")).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/users/tester")).andReturn();
    }
    
    @Test
    @WithMockUser("test")
    public void authedUserRedirectedToProfile() throws Exception {
        mockMvc.perform(get("/first")).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/successfullogin")).andReturn();
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
    public void cantCreateUserWithTooLongPassword() throws Exception {
        accountRep.deleteAll();
        MvcResult res = mockMvc.perform(post("/register").param("username", "testing")
            .param("password", "tester123tester123tester123tester123")
            .param("passwordConfirm", "tester123tester123tester123tester123")
            .param("nickname", "testering")).andReturn();
        assertTrue(res.getResponse().getContentAsString().contains("Password too long"));
    }
    
    @Test
    public void passwordsDoNotMatch() throws Exception {
        accountRep.deleteAll();
        MvcResult res = mockMvc.perform(post("/register").param("username", "testing")
            .param("password", "tester123tes")
            .param("passwordConfirm", "tester123tester")
            .param("nickname", "testering")).andReturn();
        assertTrue(res.getResponse().getContentAsString().contains("Passwords do not match"));
    }
    
    @Test
    public void cantLoginWithFakeAccount() throws Exception {
        FormLoginRequestBuilder login = formLogin().user("notfound").password("hehe");
        mockMvc.perform(login).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/login?error")).andReturn();
    }
    
    @Test
    public void showsRegisterForm() throws Exception {
        MvcResult res = mockMvc.perform(get("/register")).andReturn();
        assertTrue(res.getResponse().getContentAsString().contains("Enter information"));
    }
    
    @Test
    public void cantCreateUsernameWithExistingUsername() throws Exception {
        MvcResult res = mockMvc.perform(post("/register")
            .param("username", "test")
            .param("password", "tester123")
            .param("passwordConfirm", "tester123")
            .param("nickname", "tester"))
        .andReturn();
        assertTrue(res.getResponse().getContentAsString().contains("Username already taken"));
        assertTrue(res.getResponse().getContentAsString().contains("Nickname already taken"));
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
    @WithMockUser(username = "test", password = "tester123", roles = "USER")
    public void canSeeWallWhenLoggedIn() throws Exception {
        MvcResult res = mockMvc.perform(get("/users/tester")).andReturn();
        assertTrue(res.getResponse().getContentAsString().contains("No posts by tester"));
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
}
